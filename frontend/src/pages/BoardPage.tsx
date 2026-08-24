import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { DragDropContext, Droppable, Draggable, type DropResult } from "@hello-pangea/dnd";

interface Card {
    cardId: string;
    title: string;
    description?: string;
    position: number;
}

interface Column {
    columnId: string;
    name: string;
    position:number;
    cards?: Card[];
}

interface Board {
    boardId: string;
    boardName: string;
    owner: string;
    role: string;
    collaborators: string[];
    columns: Column[]
}

function BoardPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [board, setBoard] = useState<Board | null>(null);
    const [editingName, setEditingName] = useState(false);
    const [editingCollaborators, setEditingCollaborators] = useState(false);
    const [newName, setNewName] = useState("");
    const [newCollaborators, setNewCollaborators] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [collaboratorErrorMessage, setCollaboratorErrorMessage] = useState("");

    //For Columns and Cards
    const [newColName, setNewColName] = useState("");
    const [isAddingCol, setIsAddingCol] = useState(false);
    const [activeCardColId, setActiveCardColId] = useState<string | null>(null);
    const [newCardTitle, setNewCardTitle] = useState("");
    const [newCardDesc, setNewCardDesc] = useState("");
    const [selectedCard, setSelectedCard] = useState<Card | null>(null);
    const [editCardTitle, setEditCardTitle] = useState("");
    const [editCardDesc, setEditCardDesc] = useState("");


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
        setNewCollaborators(data.data.collaborators.join(", "));
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
    const handleAddColumn = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!newColName.trim()) return;

        const res = await fetch(`http://localhost:8080/api/board/${id}/columns`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ name: newColName }),
        });

        if (res.ok) {
            setNewColName("");
            setIsAddingCol(false);
            fetchBoard();
        }
    };

    const handleEditCollaboratorsSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        setCollaboratorErrorMessage("");

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

        if (!res.ok) {
            setCollaboratorErrorMessage(data.message);
            return;
        }

        setBoard(data.data);
        setEditingCollaborators(false);
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

    const handleDeleteColumn = async (columnId: string) => {
        if (!window.confirm("Delete this column?")) return;
        const res = await fetch(`http://localhost:8080/api/board/columns/${columnId}`, {
            method: "DELETE",
            credentials: "include",
        });

        if (res.ok) {
            fetchBoard();
        }
    };

    const onDragEnd = async (result: DropResult) => {
        const { destination, source, type, draggableId } = result;

        if (!destination || (destination.droppableId === source.droppableId && destination.index === source.index)) {
            return;
        }

        if (!board || !board.columns) return;

        // To Drag the Columns 
        if (type === "COLUMN") {
            const reordered = Array.from(board.columns);
            const [movedCol] = reordered.splice(source.index, 1);
            reordered.splice(destination.index, 0, movedCol);

            setBoard({ ...board, columns: reordered });

            await fetch(`http://localhost:8080/api/board/${board.boardId}/columns/reorder`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ columnIds: reordered.map((col) => col.columnId) }),
            });
            return;
        }

        // To Drag the Cards
        if (type === "CARD") {
            const sourceColIndex = board.columns.findIndex((c) => c.columnId === source.droppableId);
            const destColIndex = board.columns.findIndex((c) => c.columnId === destination.droppableId);

            if (sourceColIndex === -1 || destColIndex === -1) return;

            const newColumns = Array.from(board.columns);
            const sourceCards = Array.from(newColumns[sourceColIndex].cards || []);
            const destCards = sourceColIndex === destColIndex ? sourceCards : Array.from(newColumns[destColIndex].cards || []);

            const [movedCard] = sourceCards.splice(source.index, 1);
            destCards.splice(destination.index, 0, movedCard);

            newColumns[sourceColIndex].cards = sourceCards;
            newColumns[destColIndex].cards = destCards;

            setBoard({ ...board, columns: newColumns });

            await fetch(`http://localhost:8080/api/board/cards/${draggableId}/move`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({
                    targetColumnId: destination.droppableId,
                    newPosition: destination.index,
                }),
            });
        }
    };

    const handleAddCard = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!activeCardColId || !newCardTitle.trim()) return;

        const res = await fetch(`http://localhost:8080/api/board/columns/${activeCardColId}/cards`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ 
                title: newCardTitle.trim(), 
                description: newCardDesc.trim() 
            }),
        });

        if (res.ok) {
            setNewCardTitle("");
            setNewCardDesc("");
            setActiveCardColId(null);
            fetchBoard();
        }
    };
    const handleUpdateCard = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedCard || !editCardTitle.trim()) return;

        const res = await fetch(`http://localhost:8080/api/board/cards/${selectedCard.cardId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({
                title: editCardTitle.trim(),
                description: editCardDesc.trim(),
            }),
        });

        if (res.ok) {
            setSelectedCard(null);
            fetchBoard();
        }
    };

    const handleDeleteCard = async (cardId: string) => {
        const res = await fetch(`http://localhost:8080/api/board/cards/${cardId}`, {
            method: "DELETE",
            credentials: "include",
        });

        if (res.ok) {
            fetchBoard();
        }
    };

    const openEditModal = (card: Card) => {
        setSelectedCard(card);
        setEditCardTitle(card.title);
        setEditCardDesc(card.description || "");
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

                {editingCollaborators ? (
                    <form onSubmit={handleEditCollaboratorsSubmit} className="flex gap-3 items-start">
                        <input
                            type="text"
                            value={newCollaborators}
                            onChange={(e) => setNewCollaborators(e.target.value)}
                            className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                        />
                        <button type="submit" className="bg-cyan-500 text-white px-4 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer">
                            Save
                        </button>
                        <button type="button" onClick={() => {setEditingCollaborators(false); setCollaboratorErrorMessage(""); setNewCollaborators(board.collaborators.join(", "));}} className="px-4 py-2 rounded-md font-medium text-gray-600 hover:bg-gray-100 transition cursor-pointer">
                            Cancel
                        </button>
                    </form>
                ) : (
                    <div className="flex justify-between items-center">
                        {board.collaborators.length > 0 ? (
                            <p className="text-sm text-gray-500 mt-1">
                                Collaborators: {board.collaborators.map((c) => `@${c}`).join(", ")}
                            </p>
                        ) : (
                            <p className="text-sm text-gray-500 mt-1">
                                Collaborators: No collaborators
                            </p>
                        )}
                        {board.role === "owner" && (
                            <button onClick={() => setEditingCollaborators(true)} className="text-sm text-cyan-600 hover:underline cursor-pointer">
                                Edit
                            </button>
                        )}

                    </div>
                )}

                {collaboratorErrorMessage && <p className="text-red-500 text-sm mt-2">{collaboratorErrorMessage}</p>}

            </div>

            <div className="border border-gray-200 rounded-lg p-6 mb-6">
                <DragDropContext onDragEnd={onDragEnd}>
                    <Droppable droppableId="columns-container" direction="horizontal" type="COLUMN">
                        {(provided) => (
                            <div 
                                ref={provided.innerRef} 
                                {...provided.droppableProps}
                                className="flex items-start gap-4 overflow-x-auto pb-4"
                            >
                                {board.columns?.map((col, index) => (
                                    <Draggable key={col.columnId} draggableId={col.columnId} index={index}>
                                        {(providedDraggable) => (
                                            <div 
                                                ref={providedDraggable.innerRef}
                                                {...providedDraggable.draggableProps}
                                                className="w-72 bg-gray-100 rounded-lg p-3 shrink-0 shadow-sm border border-gray-200 flex flex-col"
                                            >
                                                {/* Column Header */}
                                                <div 
                                                    {...providedDraggable.dragHandleProps} 
                                                    className="flex justify-between items-center pb-2 mb-3 border-b border-gray-200 cursor-grab active:cursor-grabbing select-none"
                                                >
                                                    <h3 className="font-semibold text-gray-800 text-sm">{col.name}</h3>
                                                    <button
                                                        type="button"
                                                        onClick={() => handleDeleteColumn(col.columnId)}
                                                        className="text-gray-400 hover:text-red-500 text-xs font-bold cursor-pointer p-1"
                                                        title="Delete column"
                                                    >
                                                        ✕
                                                    </button>
                                                </div>

                                                {/* List of Cards */}
                                                <Droppable droppableId={col.columnId} type="CARD">
                                                    {(providedCardDrop) => (
                                                        <div 
                                                            ref={providedCardDrop.innerRef}
                                                            {...providedCardDrop.droppableProps}
                                                            className="flex-1 overflow-y-auto space-y-2 min-h-[40px] pr-0.5"
                                                        >
                                                            {col.cards?.map((card, cardIndex) => (
                                                                <Draggable key={card.cardId} draggableId={card.cardId} index={cardIndex}>
                                                                    {(providedCard) => (
                                                                        <div 
                                                                            ref={providedCard.innerRef}
                                                                            {...providedCard.draggableProps}
                                                                            {...providedCard.dragHandleProps}
                                                                            onClick={() => openEditModal(card)}
                                                                            className="bg-white p-3 rounded-lg shadow-sm border border-gray-200 flex justify-between items-start text-sm hover:border-cyan-400 hover:shadow transition cursor-pointer select-none group"
                                                                        >
                                                                            <div className="flex-1 min-w-0 pr-2">
                                                                                <h4 className="font-medium text-gray-800 break-words whitespace-normal leading-snug">
                                                                                    {card.title}
                                                                                </h4>
                                                                                {card.description && (
                                                                                    <p className="text-gray-500 text-xs mt-1.5 break-words line-clamp-2 leading-relaxed">
                                                                                        {card.description}
                                                                                    </p>
                                                                                )}
                                                                            </div>
                                                                            <button
                                                                                type="button"
                                                                                onClick={(e) => {
                                                                                    e.stopPropagation();
                                                                                    handleDeleteCard(card.cardId);
                                                                                }}
                                                                                className="text-gray-300 group-hover:text-gray-400 hover:!text-red-500 text-xs font-bold p-1 cursor-pointer shrink-0"
                                                                                title="Delete card"
                                                                            >
                                                                                ✕
                                                                            </button>
                                                                        </div>
                                                                    )}
                                                                </Draggable>
                                                            ))}
                                                            {providedCardDrop.placeholder}
                                                        </div>
                                                    )}
                                                </Droppable>

                                                {/* Open the Modal to Create Cards */}
                                                <button
                                                    type="button"
                                                    onClick={() => setActiveCardColId(col.columnId)}
                                                    className="w-full text-left text-xs font-medium text-gray-600 hover:text-gray-900 hover:bg-gray-200 py-1.5 px-2 rounded transition cursor-pointer flex items-center gap-1.5 mt-auto"
                                                >
                                                    <span className="text-sm font-bold">+</span> Add a card
                                                </button>
                                            </div>
                                        )}
                                    </Draggable>
                                ))}
                                {provided.placeholder}
                                    {!isAddingCol ? (
                                        <button
                                            type="button"
                                            onClick={() => setIsAddingCol(true)}
                                            className="w-72 shrink-0 bg-gray-100 hover:bg-gray-200 text-gray-700 text-sm font-medium py-3 px-4 rounded-lg flex items-center gap-2 transition cursor-pointer border border-dashed border-gray-300 hover:border-gray-400"
                                        >
                                            <span className="text-lg leading-none font-bold text-gray-500">+</span> Add column
                                        </button>
                                    ) : (
                                        <form 
                                            onSubmit={handleAddColumn} 
                                            className="w-72 shrink-0 bg-gray-100 rounded-lg p-3 flex flex-col gap-2 shadow-sm border border-gray-200"
                                        >
                                            <input
                                                type="text"
                                                placeholder="Enter column title..."
                                                value={newColName}
                                                onChange={(e) => setNewColName(e.target.value)}
                                                className="w-full text-sm p-2 border border-gray-300 rounded bg-white focus:outline-none focus:ring-1 focus:ring-cyan-500"
                                                autoFocus
                                                required
                                            />
                                            <div className="flex items-center gap-2">
                                                <button
                                                    type="submit"
                                                    className="bg-cyan-600 text-white text-xs px-3 py-1.5 rounded font-medium hover:bg-cyan-500 transition cursor-pointer"
                                                >
                                                    Add column
                                                </button>
                                                <button
                                                    type="button"
                                                    onClick={() => {
                                                        setIsAddingCol(false);
                                                        setNewColName("");
                                                    }}
                                                    className="text-xs px-3 py-1.5 rounded font-medium text-red-600 hover:bg-red-50 hover:text-red-700 transition cursor-pointer"
                                                    title="Cancel"
                                                > Cancel
                                                </button>
                                            </div>
                                        </form>
                                    )}

                            </div>
                        )}
                    </Droppable>
                </DragDropContext>               
            </div>

            {board.role === "owner" && (
                <button
                    onClick={handleDelete}
                    className="text-sm text-red-500 hover:underline cursor-pointer"
                >
                    Delete board
                </button>
            )}

            {activeCardColId && (
                <div className="fixed inset-0 bg-black/40 backdrop-blur-xs flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-xl shadow-xl w-full max-w-md p-6">
                        <h2 className="text-lg font-bold text-gray-800 mb-4">Create New Card</h2>
                        <form onSubmit={handleAddCard} className="space-y-4">
                            <div>
                                <label className="block text-xs font-semibold text-gray-600 mb-1">Title</label>
                                <input
                                    type="text"
                                    placeholder="e.g. Design Landing Page"
                                    value={newCardTitle}
                                    onChange={(e) => setNewCardTitle(e.target.value)}
                                    className="w-full text-sm p-2.5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                    autoFocus
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-semibold text-gray-600 mb-1">Description (optional)</label>
                                <textarea
                                    rows={4}
                                    placeholder="Add more details about this task..."
                                    value={newCardDesc}
                                    onChange={(e) => setNewCardDesc(e.target.value)}
                                    className="w-full text-sm p-2.5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-cyan-500 resize-none"
                                />
                            </div>

                            <div className="flex justify-end gap-2 pt-2">
                                <button
                                    type="button"
                                    onClick={() => {
                                        setActiveCardColId(null);
                                        setNewCardTitle("");
                                        setNewCardDesc("");
                                    }}
                                    className="text-xs px-4 py-2 rounded-lg font-medium text-red-600 hover:bg-red-50 transition cursor-pointer"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="bg-cyan-600 text-white text-xs px-4 py-2 rounded-lg font-medium hover:bg-cyan-500 transition cursor-pointer"
                                >
                                    Create Card
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {/* Viewing and the Editing of Cards  */}
            {selectedCard && (
                <div className="fixed inset-0 bg-black/40 backdrop-blur-xs flex items-center justify-center p-4 z-50">
                    <div className="bg-white rounded-xl shadow-xl w-full max-w-lg p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-lg font-bold text-gray-800">Card Details</h2>
                            <button
                                type="button"
                                onClick={() => setSelectedCard(null)}
                                className="text-gray-400 hover:text-gray-600 text-lg font-bold cursor-pointer"
                            >
                                ✕
                            </button>
                        </div>
                        <form onSubmit={handleUpdateCard} className="space-y-4">
                            <div>
                                <label className="block text-xs font-semibold text-gray-600 mb-1">Title</label>
                                <input
                                    type="text"
                                    value={editCardTitle}
                                    onChange={(e) => setEditCardTitle(e.target.value)}
                                    className="w-full text-sm p-2.5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-cyan-500 font-medium text-gray-800"
                                    required
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-semibold text-gray-600 mb-1">Description</label>
                                <textarea
                                    rows={5}
                                    placeholder="Add a more detailed description..."
                                    value={editCardDesc}
                                    onChange={(e) => setEditCardDesc(e.target.value)}
                                    className="w-full text-sm p-2.5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-cyan-500 text-gray-700 leading-relaxed"
                                />
                            </div>

                            <div className="flex justify-between items-center pt-2">
                                <button
                                    type="button"
                                    onClick={() => handleDeleteCard(selectedCard.cardId)}
                                    className="text-xs px-3 py-2 rounded-lg font-medium text-red-600 hover:bg-red-50 transition cursor-pointer"
                                >
                                    Delete Card
                                </button>
                                <div className="flex gap-2">
                                    <button
                                        type="button"
                                        onClick={() => setSelectedCard(null)}
                                        className="text-xs px-4 py-2 rounded-lg font-medium text-gray-600 hover:bg-gray-100 transition cursor-pointer"
                                    >
                                        Close
                                    </button>
                                    <button
                                        type="submit"
                                        className="bg-cyan-600 text-white text-xs px-4 py-2 rounded-lg font-medium hover:bg-cyan-500 transition cursor-pointer"
                                    >
                                        Save Changes
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default BoardPage;