package com.kanbanboard.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kanbanboard.backend.dto.BoardDTO;
import com.kanbanboard.backend.dto.CreateBoardRequest;
import com.kanbanboard.backend.dto.Response;
import com.kanbanboard.backend.entity.Board;
import com.kanbanboard.backend.entity.User;
import com.kanbanboard.backend.repo.BoardRepository;
import com.kanbanboard.backend.repo.UserRepository;

@Service
public class BoardService {
    private final UserRepository userRepo;
    private final BoardRepository boardRepo;

    public BoardService(UserRepository userRepo, BoardRepository boardRepo) {
        this.userRepo = userRepo;
        this.boardRepo = boardRepo;
    }

    // validation calls
    public boolean isBoardNameValid(String boardName) {
        return boardName != null && !boardName.isBlank() && boardName.length() <= 25 && !boardName.matches(".*[<>].*");
    }


    // return if this user is either owner or collaborator of this board
    public boolean hasAccess(Board board, UUID userId) {
        if (isOwner(board, userId)) {
            return true;
        }
        for (User currUser : board.getCollaborators()) {
            if (currUser.getUserid().equals(userId)) {
                return true;
            }
        }
        return false; // no match, no access
    }

    // return if this user is the board's owner
    public boolean isOwner(Board board, UUID userId) {
        return board.getOwner().getUserid().equals(userId);
    }

    public List<User> validCollaborators(List<String> collaborators) {
        List<User> users = new ArrayList<>();
        for (String currUsername : collaborators) {
            User currUser = userRepo.findByUsername(currUsername)
                .orElseThrow(() -> new RuntimeException("Collaborator not found: " + currUsername));
            users.add(currUser);
        }
        return users;
    }
    
    public Response<BoardDTO> createBoard(CreateBoardRequest request, UUID ownerId) {
        Response<BoardDTO> res;
        String boardName = request.getBoardName();
        List<String> collaborators = request.getCollaborators();
        List<User> collaboratorUsers;

        if (!isBoardNameValid(boardName)) {
            res = new Response<>(400, "Board name must be between 1 and 25 characters");
            return res;
        }

        User owner = userRepo.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found on creating a board"));

        if (collaborators == null) {
            collaboratorUsers = new ArrayList<>();
        }
        else {
            // collaborators validation check
            if (collaborators.contains(owner.getUsername())) {
                res = new Response<>(400, "Owner shouldn't self invite to be a collaborator");
                return res;
            }
            try {
                collaboratorUsers = validCollaborators(collaborators);
            } catch (RuntimeException e) {
                res = new Response<>(400, e.getMessage());
                return res;
            }
        }

        Board newBoard = new Board(boardName, owner, collaboratorUsers);
        Board savedBoard = boardRepo.save(newBoard);

        BoardDTO boardDTO = new BoardDTO(savedBoard, owner.getUsername());

        res = new Response<>(200, "Board is successfully created", boardDTO);
        return res;
    }

    public Response<List<BoardDTO>> getAllBoards(UUID userId) {
        Response<List<BoardDTO>> res;
        User currUser;

        try {
            currUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("This user does not exist for boards."));
        }
        catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        List<Board> ownerBoards = boardRepo.findByOwner(currUser); //get all board from this curr user as owner
        List<Board> collabBoards = boardRepo.findByCollaboratorsContaining(currUser); // get all board from this user exist as collaborators

        List<Board> allBoards = new ArrayList<>();
        allBoards.addAll(ownerBoards);
        allBoards.addAll(collabBoards);

        List<BoardDTO> allBoardDTO = new ArrayList<>();
        for (Board currBoard : allBoards) {
            allBoardDTO.add(new BoardDTO(currBoard, currUser.getUsername()));      
        }

        res = new Response<>(200, "All boards successfully retrieved", allBoardDTO);
        return res;
    }

    public Response<BoardDTO> getOneBoard(UUID boardId, UUID userId) {
        Response<BoardDTO> res;
        Board currBoard;
        User currUser;

        try {
            currBoard = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("This board does not exist"));
            currUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found in getting a board."));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        //authorization
        boolean doUserHasAccess = hasAccess(currBoard, userId);
        if (!doUserHasAccess) {
            res = new Response<>(403, "You do not have access to this board");
            return res;
        }
        
        BoardDTO currBoardDTO = new BoardDTO(currBoard, currUser.getUsername());
        res = new Response<>(200, "Successfully open this board", currBoardDTO);
        return res;
    }

    // change Board Name
    public Response<BoardDTO> changeBoardName(UUID boardId, UUID userId, String newBoardName) {
        Response<BoardDTO> res;
        Board currBoard;
        User currUser;

        try {
            currBoard = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Fail to get board in change board name"));
            currUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Fail to get the curr user in change board name"));
        }   
        catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        //autherization anyone who has access
        if (!hasAccess(currBoard, userId)) {
            res = new Response<>(403, "You do not have access to modify the name of this board");
            return res;
        }

        //validate new board name
        if (!isBoardNameValid(newBoardName)) {
            res = new Response<>(400, "Board name must be between 1 and 25 characters");
            return res;
        }

        currBoard.setBoardName(newBoardName);
        boardRepo.save(currBoard); //update currBoard since it alr have an id

        BoardDTO boardDTO = new BoardDTO(currBoard, currUser.getUsername());
        res = new Response<>(200, "Board name successfully updated", boardDTO);
        return res;
    }

    public Response<String> deleteABoard(UUID boardId, UUID userId) {
        Response<String> res;
        Board currBoard;

        //validate board first
        try {
            currBoard = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board does not exist on deleting a board"));
        }
        catch (RuntimeException e) {
            res = new Response<>(404, "User or board does not exist to delete a board");
            return res;
        }

        // validate -> only owner is authorize to delete a board
        if (!isOwner(currBoard, userId)) {
            res = new Response<>(403, "Only owner has permission to delete the board!");
            return res;
        }

        // delete the board
        boardRepo.delete(currBoard);
        res = new Response<>(200, "Board is successfully deleted");
        return res;
    }

}
