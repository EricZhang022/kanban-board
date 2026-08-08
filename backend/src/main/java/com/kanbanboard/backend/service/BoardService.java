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
        return boardName != null && !boardName.isBlank() && boardName.length() <= 25;
    }

    public boolean isOwnerInCollaborators(String ownerUsername, List<String> collaborators) {
        return collaborators.contains(ownerUsername);
    }

    // return if this user is either owner or collaborator of this board
    public boolean hasAccess(Board board, String username) {
        if (isOwner(board, username)) {
            return true;
        }
        for (User currUser : board.getCollaborators()) {
            if (currUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false; // no match, no access
    }

    // return if this user is the board's owner
    public boolean isOwner(Board board, String username) {
        return board.getOwner().getUsername().equals(username);
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
    
    public Response<BoardDTO> createBoard(CreateBoardRequest request, String ownerUsername) {
        Response<BoardDTO> res;
        String boardName = request.getBoardName();
        List<String> collaborators = request.getCollaborators();
        List<User> collaboratorUsers;

        if (!isBoardNameValid(boardName)) {
            res = new Response<>(400, "Board name must be between 1 and 25 characters");
            return res;
        }

        if (collaborators == null) {
            collaboratorUsers = new ArrayList<>();
        }
        else {
            // collaborators validation check
            if (isOwnerInCollaborators(ownerUsername, collaborators)) {
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

        User owner = userRepo.findByUsername(ownerUsername)
            .orElseThrow(() -> new RuntimeException("Owner not found"));

        Board newBoard = new Board(boardName, owner, collaboratorUsers);
        Board savedBoard = boardRepo.save(newBoard);

        BoardDTO boardDTO = new BoardDTO(savedBoard, owner.getUsername());

        res = new Response<>(200, "Board is successfully created", boardDTO);
        return res;
    }

    public Response<List<BoardDTO>> getAllBoards(String username) {
        Response<List<BoardDTO>> res;
        User currUser;

        try {
            currUser = userRepo.findByUsername(username)
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
            allBoardDTO.add(new BoardDTO(currBoard, username));      
        }

        res = new Response<>(200, "All boards successfully retrieved", allBoardDTO);
        return res;
    }

    public Response<BoardDTO> getOneBoard(UUID boardId, String username) {
        Response<BoardDTO> res;
        Board currBoard;

        try {
            currBoard = boardRepo.findById(boardId)
                .orElseThrow(() -> new RuntimeException("This board does not exist"));
        } catch (RuntimeException e) {
            res = new Response<>(404, e.getMessage());
            return res;
        }

        //authorization
        boolean doUserHasAccess = hasAccess(currBoard, username);
        if (!doUserHasAccess) {
            res = new Response<>(403, "You do not have access to this board");
            return res;
        }
        
        BoardDTO currBoardDTO = new BoardDTO(currBoard, username);
        res = new Response<>(200, "Successfully open this board", currBoardDTO);
        return res;
    }
}
