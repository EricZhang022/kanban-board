package com.kanbanboard.backend.service;

import java.util.ArrayList;
import java.util.List;

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

        BoardDTO boardDTO = new BoardDTO(savedBoard);

        res = new Response<>(200, "Board is successfully created", boardDTO);
        return res;
    }
}
