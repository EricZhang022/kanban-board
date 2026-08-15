import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router";

interface Board {
    boardId: string;
    boardName: string;
    owner: string;
    role: string;
    collaborators: string[];
}

function Dashboard() {
    const navigate = useNavigate();
    const [boards, setBoards] = useState<Board[]>([]);
    const [openMenuId, setOpenMenuId] = useState<string | null>(null);
    const menuRef = useRef<HTMLDivElement>(null);

    // get all Boards for this user
    const fetchBoards = async () => {
        const res = await fetch("http://localhost:8080/api/board", {
            credentials: "include",
        });
        const data = await res.json();
        setBoards(data.data || []);
    };

    useEffect(() => {
        fetchBoards();
    }, []);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
                setOpenMenuId(null);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    // delete a board - only ever called with the ID of the board whose "Delete" was clicked
    const handleDelete = async (boardId: string) => {
        const confirmed = window.confirm("Delete this board? This can't be undone.");
        if (!confirmed) return;

        const res = await fetch(`http://localhost:8080/api/board/${boardId}`, {
            method: "DELETE",
            credentials: "include",
        });

        if (res.ok) {
            setOpenMenuId(null);
            fetchBoards();
        }
    };

    return (
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10">
            <div className="flex justify-between items-center mb-4 sm:mb-6">
                <h1 className="text-xl sm:text-2xl lg:text-3xl font-bold text-gray-800">
                    Your Boards
                </h1>
                <button
                    onClick={() => navigate("/createboard")}
                    className="bg-cyan-500 text-white px-4 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                >
                    + New Board
                </button>
            </div>

            <div className="border border-gray-200 rounded-lg p-4 sm:p-6">
                {boards.length === 0 ? (
                    <p className="text-gray-500">No boards yet — create one above.</p>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                        {boards.map((board) => (
                            <div
                                key={board.boardId}
                                onClick={() => navigate(`/board/${board.boardId}`)}
                                className="relative flex flex-col bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition cursor-pointer"
                            >
                                {/* Top row: name, role, ... menu — this button only ever affects THIS board's id */}
                                <div className="flex justify-between items-start">
                                    <div>
                                        <h3 className="font-semibold text-gray-800">{board.boardName}</h3>
                                        <p className="text-sm text-gray-500 mt-1">
                                            {board.role === "owner" ? "Owner" : `Owned by @${board.owner}`}
                                        </p>
                                    </div>

                                    <button
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            setOpenMenuId(openMenuId === board.boardId ? null : board.boardId);
                                        }}
                                        className="text-gray-400 hover:text-gray-600 px-2 cursor-pointer"
                                    >
                                        ⋯
                                    </button>
                                </div>

                                {/* Only renders for the ONE board whose id matches openMenuId */}
                                {openMenuId === board.boardId && (
                                    <div
                                        ref={menuRef}
                                        onClick={(e) => e.stopPropagation()}
                                        className="absolute right-4 top-10 w-32 bg-white border border-gray-200 rounded-md shadow-lg overflow-hidden z-10"
                                    >
                                        {board.role === "owner" ? (
                                            <button
                                                onClick={() => handleDelete(board.boardId)}
                                                className="w-full text-left px-4 py-2 text-sm text-red-500 hover:bg-red-50 transition cursor-pointer"
                                            >
                                                Delete
                                            </button>
                                        ) : (
                                            <p className="px-4 py-2 text-sm text-gray-400">No options</p>
                                        )}
                                    </div>
                                )}

                                {/* Middle: icon */}
                                <div className="flex-1 flex items-center justify-center py-6 sm:py-8">
                                    <svg
                                        className="w-10 h-10 sm:w-12 sm:h-12 text-cyan-500"
                                        fill="none"
                                        viewBox="0 0 24 24"
                                        stroke="currentColor"
                                        strokeWidth={1.5}
                                    >
                                        <rect x="3" y="4" width="18" height="16" rx="2" />
                                        <line x1="9" y1="4" x2="9" y2="20" />
                                        <line x1="15" y1="4" x2="15" y2="20" />
                                    </svg>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default Dashboard;