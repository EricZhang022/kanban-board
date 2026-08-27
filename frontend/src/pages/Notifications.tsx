import { useEffect, useState } from "react";

interface User {
    userId: string;
    username: string;
    firstName: string;
    lastName: string;
}

interface Notification {
    notificationId: string;
    type: string;
    sender: User | null;
    read: boolean;
    createdAt: string;
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

    return (
        <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-6 sm:py-10">
            <div className="flex justify-between items-center mb-4 sm:mb-6">
                <h1 className="text-xl sm:text-2xl lg:text-3xl font-bold text-gray-800">
                    Your Notifications
                </h1>
            </div>

             <div className="bg-white rounded-lg shadow-md overflow-hidden">
                {notifications.length === 0 ? (
                    <p className="p-6 text-gray-500 text-center">
                        You have no notifications.
                    </p>
                ) : (
                    notifications.map((notification) => (
                        <div
                            key={notification.notificationId}
                            className={`px-6 py-4 border-b last:border-b-0 ${
                                notification.read
                                    ? "bg-white"
                                    : "bg-indigo-50"
                            }`}
                        >
                            <div className="flex justify-between items-center">
                                <div>
                                    <p className="font-medium text-gray-800">
                                        {notification.sender
                                            ? `@${notification.sender.username}`
                                            : "System"}
                                    </p>

                                    <p className="text-sm text-gray-600 mt-1">
                                        {notification.type}
                                    </p>
                                </div>

                                <span className="text-sm text-gray-400">
                                    {new Date(
                                        notification.createdAt
                                    ).toLocaleString()}
                                </span>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}

export default Notifications