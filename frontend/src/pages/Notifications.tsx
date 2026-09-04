import { useEffect, useState } from "react";
import { Check, X } from "lucide-react";

interface User {
    userId: string;
    username: string;
    firstName: string;
    lastName: string;
}

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

interface Notification {
    notificationId: string;
    type: string;
    sender: User | null;
    board: Board | null;
    read: boolean;
    createdAt: string;
    invitationId: string | null;
    invitationStatus: "PENDING" | "ACCEPTED" | "DECLINED" | "EXPIRED" | null;
}

function getNotificationMessage(notification: Notification) {
    const sender = notification.sender
        ? `@${notification.sender.username}`
        : "The system";

    const board = notification.board
        ? `${notification.board.boardName}`
        : "N/A";

    const role = notification.board
        ? `${notification.board.role}`
        : "collab";

    switch (notification.type) {
        case "BOARD_INVITATION":
            if (notification.invitationStatus === "ACCEPTED") {
                return `You accepted an invitation from ${sender} to join "${board}."`;
            }

            if (notification.invitationStatus === "DECLINED") {
                return `You declined an invitation from ${sender} to join "${board}."`;
            }

            if (notification.invitationStatus === "EXPIRED") {
                return `The invitation to join "${board} has expired.`;
            }

            return `${sender} invited you to a board named "${board}" to be a ${role}.`;

        case "BOARD_INVITATION_ACCEPTED":
            return `${sender} accepted your invitation to join "${board}".`;

        case "BOARD_INVITATION_DECLINED":
            return `${sender} declined your invitation to join "${board}".`;
            
        case "COLLABORATOR_REMOVED":
            return `You were removed as a ${role} from the board "${board}".`

        case "TASK_ASSIGNED":
            return `${sender} assigned you a task.`;

        case "MENTION":
            return `${sender} mentioned you.`;

        default:
            return "You have a new notification.";
    }
}

function Notifications() {

    const [notifications, setNotifications] = useState<Notification[]>([]);

    const fetchNotifications = async () => {
        const res = await fetch("http://localhost:8080/api/notifications", {
            credentials: "include",
        });

        const data = await res.json();
        setNotifications(data.data || []);
    };

    useEffect(() => {
        fetchNotifications();
    }, []);

    const markAllAsRead = async () => {
        const res = await fetch(
            "http://localhost:8080/api/notifications/read",
            {
                method: "PATCH",
                credentials: "include",
            }
        );

        if (!res.ok) {
            console.error("Failed to mark all notifications as read");
            return;
        }

        // Mark all notifications as read except board invitations
        setNotifications((prev) =>
            prev.map((notification) =>
                notification.type === "BOARD_INVITATION"
                    ? notification
                    : { ...notification, read: true }
            )
        );
    };

    const markAsRead = async (notificationId: string) => {
        const res = await fetch(
            `http://localhost:8080/api/notifications/read/${notificationId}`,
            {
                method: "PATCH",
                credentials: "include",
            }
        );

        if (!res.ok) {
            console.error("Failed to mark notification as read");
            return;
        }

        setNotifications((prev) => {
            const updated = prev.map((notification) =>
                notification.notificationId === notificationId
                    ? { ...notification, read: true }
                    : notification
            );

            return updated.sort((a, b) => {
                // Unread notifications first
                if (a.read !== b.read) {
                    return a.read ? 1 : -1;
                }

                // Newest first within each group
                return (
                    new Date(b.createdAt).getTime() -
                    new Date(a.createdAt).getTime()
                );
            });
        });
    };

    const deleteAllReadNotifications = async () => {
        const res = await fetch(
            "http://localhost:8080/api/notifications/read",
            {
                method: "DELETE",
                credentials: "include",
            }
        );

        if (!res.ok) {
            console.error("Failed to delete read notifications");
            return;
        }

        setNotifications((prev) =>
            prev.filter((notification) => !notification.read)
        );
    };

    const deleteNotification = async (notificationId: string) => {
        const res = await fetch(
            `http://localhost:8080/api/notifications/read/${notificationId}`,
            {
                method: "DELETE",
                credentials: "include",
            }
        );

        if (!res.ok) {
            console.error("Failed to delete notification");
            return;
        }

        setNotifications((prev) =>
            prev.filter(
                (notification) =>
                    notification.notificationId !== notificationId
            )
        );
    };

    const acceptInvitation = async (invitationId: string) => {
        const response = await fetch(
            `http://localhost:8080/api/invitations/accept/${invitationId}`,
            {
                method: "POST",
                credentials: "include",
            }
        );

        if (!response.ok) {
            fetchNotifications();
            return;
        }

        // Reload notifications
        fetchNotifications();
    };

    const declineInvitation = async (invitationId: string) => {
        const response = await fetch(
            `http://localhost:8080/api/invitations/decline/${invitationId}`,
            {
                method: "POST",
                credentials: "include",
            }
        );

        if (!response.ok) {
            fetchNotifications();
            return;
        }

        // Reload notifications
        fetchNotifications();
    };

    return (
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10">
            <div className="flex justify-between items-center mb-4 sm:mb-6">
                <h1 className="text-xl sm:text-2xl lg:text-3xl font-bold text-gray-800">
                    Your Notifications
                </h1>
            </div>

            <div className="flex gap-2 mb-5">
                <button
                    onClick={markAllAsRead}
                    className="px-4 py-2 text-sm bg-cyan-500 text-white rounded-md hover:bg-cyan-400 transition cursor-pointer"
                >
                    Mark all as read
                </button>

                <button
                    onClick={deleteAllReadNotifications}
                    className="px-4 py-2 text-sm bg-red-400 text-white rounded-md hover:bg-red-300 transition cursor-pointer"
                >
                    Delete all read
                </button>
            </div>

            <div className="bg-white rounded-lg shadow-md overflow-hidden">
                {notifications.length === 0 ? (
                    <p className="p-6 text-gray-500 text-center">
                        You have no notifications.
                    </p>
                ) : (
                    <>
                        {notifications.map((notification) => (
                            <div
                                key={notification.notificationId}
                                onClick={() => {
                                    if (notification.type !== "BOARD_INVITATION" && !notification.read) {
                                        markAsRead(notification.notificationId);
                                    }
                                }}
                                className={`px-6 py-4 border-b last:border-b-0 cursor-pointer transition ${
                                    notification.read
                                        ? "bg-gray-200 hover:bg-gray-300"
                                        : "bg-white hover:bg-gray-100"
                                }`}
                            >
                                <div className="flex justify-between items-center">
                                    <p className="text-sm text-gray-600 mt-1">
                                        {getNotificationMessage(notification)}
                                    </p>

                                    <div className="flex items-center gap-3">
                                        <span className="text-sm text-gray-400">
                                            {new Date(
                                                notification.createdAt
                                            ).toLocaleString()}
                                        </span>

                                        {notification.type === "BOARD_INVITATION" && 
                                            notification.invitationStatus === "PENDING" && 
                                            notification.invitationId && (
                                            <div className="flex items-center gap-2">
                                                <button
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        acceptInvitation(notification.invitationId!);
                                                    }}
                                                    className="text-green-600 hover:text-green-800 transition cursor-pointer"
                                                >
                                                    <Check size={18} />
                                                </button>

                                                <button
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        declineInvitation(notification.invitationId!);
                                                    }}
                                                    className="text-red-500 hover:text-red-700 transition cursor-pointer"
                                                >
                                                    <X size={18} />
                                                </button>
                                            </div>
                                        )}

                                        {notification.read && (
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    deleteNotification(
                                                        notification.notificationId
                                                    );
                                                }}
                                                className="text-gray-400 hover:text-gray-600 transition cursor-pointer"
                                            >
                                                <X size={18} />
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </>
                )}
            </div>
        </div>
    );
}

export default Notifications;