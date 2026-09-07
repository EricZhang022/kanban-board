import { createContext, useContext, useEffect, useState, type ReactNode } from "react";

interface NotificationContextType {
    unreadCount: number;
    refreshUnreadCount: () => Promise<void>;
}

const NotificationContext = createContext<
    NotificationContextType | undefined
>(undefined);

export const NotificationProvider = ({
    children,
}: {
    children: ReactNode;
}) => {
    const [unreadCount, setUnreadCount] = useState(0);

    const refreshUnreadCount = async () => {
        try {
            const res = await fetch(
                "http://localhost:8080/api/notifications/unread-count",
                {
                    credentials: "include",
                }
            );

            if (!res.ok) {
                console.error("Failed to fetch unread notification count");
                return;
            }

            const data = await res.json();

            setUnreadCount(data.data);
        } catch (error) {
            console.error("Failed to fetch unread notification count:", error);
        }
    };

    // Fetch unread count when provider mounts
    useEffect(() => {
        refreshUnreadCount();
    }, []);

    return (
        <NotificationContext.Provider
            value={{
                unreadCount,
                refreshUnreadCount,
            }}
        >
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotificationContext = () => {
    const context = useContext(NotificationContext);

    if (!context) {
        throw new Error(
            "useNotificationContext must be used within a NotificationProvider"
        );
    }

    return context;
};