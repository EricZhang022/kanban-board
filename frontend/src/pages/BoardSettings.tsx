import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { X } from "lucide-react";

interface User {
    userId: string;
    firstName: string;
    lastName: string;
    username: string;
}

interface Board {
    boardId: string;
    boardName: string;
    owner: string;
    role: string;
    collaborators: User[];
}

interface Invitation {
    invitationId: string;
    recipient: User;
}

function BoardSettings() {

    const { id } = useParams();
    const navigate = useNavigate();
    const [editingName, setEditingName] = useState(false);
    const [newName, setNewName] = useState("");
    const [board, setBoard] = useState<Board | null>(null);
    const [pendingInvitations, setPendingInvitations] = useState<Invitation[] | null>(null);

    const [errorMessage, setErrorMessage] = useState("");
    const [newCollaborators, setNewCollaborators] = useState("");

    const [collaboratorMessage, setCollaboratorMessage] = useState("");
    const [collaboratorSuccess, setCollaboratorSuccess] = useState(false);

    const fetchBoard = async () => {
        const res = await fetch(`http://localhost:8080/api/board/${id}`, {
            credentials: "include",
        });

        if (!res.ok) {
            navigate("/dashboard");
            return;
        }

        const data = await res.json();
        setBoard(data.data);
        setNewName(data.data.boardName);
        setNewCollaborators(
            data.data.collaborators
                .map((collaborator: User) => collaborator.username)
                .join(", ")
        );
        setPendingInvitations(data.data.pendingInvitations);
    };

    useEffect(() => {
        fetchBoard();
    }, [id]);

    const handleRenameSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setErrorMessage("");

        const res = await fetch(`http://localhost:8080/api/board/${id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ boardName: newName }),
        });

        const data = await res.json();

        if (!res.ok) {
            setErrorMessage(data.message);
            return;
        }

        setBoard(data.data);
        setEditingName(false);
    };

    const handleEditCollaboratorsSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        const collaborators = newCollaborators
            .split(",")
            .map((name) => name.trim())
            .filter((name) => name.length > 0);

        const res = await fetch(`http://localhost:8080/api/board/collab/${id}`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ collaborators: collaborators.length > 0 ? collaborators : null, }),
        });

        const data = await res.json();

        setCollaboratorMessage(data.message);

        if (!res.ok) {
            setCollaboratorSuccess(false);
            return;
        }

        setCollaboratorSuccess(true);
        setBoard(data.data);
        setPendingInvitations(data.data.pendingInvitations);
    };

    const handleCancelInvitation = async (invitationId: string) => {
        const res = await fetch(
            `http://localhost:8080/api/invitations/${invitationId}/cancel`,
            {
                method: "PATCH",
                credentials: "include",
            }
        );

        const data = await res.json();

        if (!res.ok) {
            return;
        }

        // Refresh board + pending invitations
        fetchBoard();
        setPendingInvitations(data.data.pendingInvitations);
    }

    const handleDelete = async () => {
        const confirmed = window.confirm("Delete this board? This can't be undone.");
        if (!confirmed) return;

        const res = await fetch(`http://localhost:8080/api/board/${id}`, {
            method: "DELETE",
            credentials: "include",
        });

        if (res.ok) {
            navigate("/dashboard");
        }
    };

    if (!board) {
        return <div className="max-w-4xl mx-auto px-4 py-10">Loading...</div>;
    }

    return (
        <div className="max-w-4xl mx-auto px-4 sm:px-6 py-6 sm:py-10">
            <button
                onClick={() => navigate(`/board/${board.boardId}`)}
                className="text-sm text-gray-500 hover:text-gray-700 mb-4 cursor-pointer"
            >
                ← Back to board
            </button>

            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
                {editingName ? (
                    <form onSubmit={handleRenameSubmit} className="flex gap-3 items-start">
                        <input
                            type="text"
                            value={newName}
                            onChange={(e) => setNewName(e.target.value)}
                            className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                            required
                        />
                        <button type="submit" className="bg-cyan-500 text-white px-4 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer">
                            Save
                        </button>
                        <button type="button" onClick={() => setEditingName(false)} className="px-4 py-2 rounded-md font-medium text-gray-600 hover:bg-gray-100 transition cursor-pointer">
                            Cancel
                        </button>
                    </form>
                ) : (
                    <div className="flex justify-between items-center">
                        <h1 className="text-xl sm:text-2xl font-bold text-gray-800">{board.boardName}</h1>
                        <button onClick={() => setEditingName(true)} className="text-sm text-cyan-600 hover:underline cursor-pointer">
                            Rename
                        </button>
                    </div>
                )}

                {errorMessage && <p className="text-red-500 text-sm mt-2">{errorMessage}</p>}

                <p className="text-sm text-gray-500 mt-3">Owner: @{board.owner}</p>

            </div>

            <div className="bg-white rounded-lg shadow-md px-6 py-2 mb-6">
                <form onSubmit={handleEditCollaboratorsSubmit} className="mb-4">
                    <label htmlFor="collaborators" className="block text-md font-medium text-gray-700 mb-1">Collaborators: (comma-separated usernames)</label>
                    <div className="flex gap-3">
                        <input id="collaborators" type="text" value={newCollaborators} onChange={(e) => setNewCollaborators(e.target.value)} className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"/>
                        <button type="submit" className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer">Save</button>
                    </div>
                </form>

                {collaboratorMessage &&  <p className={collaboratorSuccess ? "text-green-500 text-sm break-words" : "text-red-500 text-sm break-words"}>{collaboratorMessage}</p>}
            </div>

            {board.collaborators.length > 0 && (
                <div className="bg-white rounded-lg shadow-md px-6 py-2 mb-6">
                    <p className="block text-md font-medium text-gray-700">
                        Invited Collaborators
                    </p>

                    <div className="space-y-4 my-4">
                        {board.collaborators.map((collaborator) => (
                            <div
                                key={collaborator.userId}
                                className="flex items-center gap-3"
                            >
                            
                                <div className="w-10 h-10 rounded-full bg-cyan-100 flex items-center justify-center">
                                    <span className="text-sm font-semibold text-cyan-700">
                                        {collaborator.firstName[0]}
                                        {collaborator.lastName[0]}
                                    </span>
                                </div>

                                <div>
                                    <p className="text-sm font-medium text-gray-800">
                                        {collaborator.firstName} {collaborator.lastName}
                                    </p>

                                    <p className="text-sm text-gray-500">
                                        @{collaborator.username}
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {pendingInvitations && pendingInvitations.length > 0 && (
                <div className="bg-white rounded-lg shadow-md px-6 py-4 mb-6">
                    <p className="text-md font-medium text-gray-700 mb-4">
                        Pending Invitations
                    </p>

                    <div className="space-y-4 mt-4 mb-2">
                        {pendingInvitations.map((invitation) => (
                            <div
                                key={invitation.invitationId}
                                className="flex items-center justify-between"
                            >
                                <div className="flex items-center gap-3">
                                    {/* Initials */}
                                    <div className="w-10 h-10 rounded-full bg-gray-100 flex items-center justify-center">
                                        <span className="text-sm font-semibold text-gray-600">
                                            {invitation.recipient.firstName[0]}
                                            {invitation.recipient.lastName[0]}
                                        </span>
                                    </div>

                                    {/* Name + username */}
                                    <div>
                                        <p className="text-sm font-medium text-gray-800">
                                            {invitation.recipient.firstName}{" "}
                                            {invitation.recipient.lastName}
                                        </p>

                                        <p className="text-sm text-gray-500">
                                            @{invitation.recipient.username}
                                        </p>
                                    </div>
                                </div>

                                <button
                                    type="button"
                                    onClick={() => handleCancelInvitation(invitation.invitationId)}
                                    className="p-2 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-full transition cursor-pointer"
                                    title="Cancel Invitation"
                                >
                                    <X size={18} />
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            )}

            {board.role === "owner" && (
                <button
                    onClick={handleDelete}
                    className="text-sm text-red-500 hover:underline cursor-pointer"
                >
                    Delete board
                </button>
            )}

        </div>
    );
}

export default BoardSettings;