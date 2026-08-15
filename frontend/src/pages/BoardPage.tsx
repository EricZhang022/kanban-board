import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";

interface Board {
    boardId: string;
    boardName: string;
    owner: string;
    role: string;
    collaborators: string[];
}

function BoardPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState<Board | null>(null);
    const [editingName, setEditingName] = useState(false);
    const [newName, setNewName] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

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
                onClick={() => navigate("/dashboard")}
                className="text-sm text-gray-500 hover:text-gray-700 mb-4 cursor-pointer"
            >
                ← Back to boards
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
                {board.collaborators.length > 0 && (
                    <p className="text-sm text-gray-500 mt-1">
                        Collaborators: {board.collaborators.map((c) => `@${c}`).join(", ")}
                    </p>
                )}
            </div>

            <div className="border border-gray-200 rounded-lg p-6 mb-6">
                <p className="text-gray-500">Columns and cards will go here.</p>
            </div>

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

export default BoardPage;