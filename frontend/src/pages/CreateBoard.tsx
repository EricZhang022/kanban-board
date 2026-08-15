import { useState } from "react";
import { useNavigate } from "react-router";

function CreateBoard() {

    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState("");
    const [boardName, setBoardName] = useState("");
    const [collaboratorsInput, setCollaboratorsInput] = useState("");

    // create a board
    const handleCreateSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setErrorMessage("");

        const collaborators = collaboratorsInput
            .split(",")
            .map((name) => name.trim())
            .filter((name) => name.length > 0);

        const res = await fetch("http://localhost:8080/api/board/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({
                boardName,
                collaborators: collaborators.length > 0 ? collaborators : null,
            }),
        });

        const data = await res.json();

        if (!res.ok) {
            setErrorMessage(data.message);
            return;
        }

        navigate("/dashboard");
    };

    return (
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10">
            <div className="text-center">
                <h1 className="text-xl sm:text-2xl lg:text-3xl font-bold text-gray-900">
                    Create a Board
                </h1>
            </div>

            <form onSubmit={handleCreateSubmit} className="bg-white rounded-lg shadow-md p-6 mt-6 max-w-lg mx-auto">
                <div className="mb-4">
                    <label htmlFor="boardName" className="block text-sm font-medium text-gray-700 mb-1">
                        Board Name
                    </label>
                    <input
                        id="boardName"
                        type="text"
                        value={boardName}
                        onChange={(e) => setBoardName(e.target.value)}
                        className="w-full border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="collaborators" className="block text-sm font-medium text-gray-700 mb-1">
                        Collaborators (comma-separated usernames, optional)
                    </label>
                    <input
                        id="collaborators"
                        type="text"
                        value={collaboratorsInput}
                        onChange={(e) => setCollaboratorsInput(e.target.value)}
                        placeholder="alice, bob"
                        className="w-full border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                    />
                </div>

                {errorMessage && (
                    <p className="text-red-500 text-sm mb-4 text-center">{errorMessage}</p>
                )}

                <div className="flex justify-center">
                    <button
                        type="submit"
                        className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                    >
                        Create
                    </button>
                </div>
            </form>
        </div>
    );
}

export default CreateBoard;