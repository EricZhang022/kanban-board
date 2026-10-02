import { Client } from "@stomp/stompjs";
import { useEffect, useState } from "react";

type PresenceUser = {
    userId: string;
    firstName: string;
    lastName: string;
    username: string;
    profileColor: string;
};

const useWebSocket = (boardId?: string) => {

    const [onlineUsers, setOnlineUsers] = useState<PresenceUser[]>([]);

    useEffect(() => {

        if (!boardId) return;

        const client = new Client({
            brokerURL: "ws://localhost:8080/ws",
            
            // When WebSocket connects
            onConnect: () => {
                // Connect to this endpoint to get online presence of this board
                client.subscribe(
                    `/topic/boards/${boardId}/presence`,
                    (message) => {

                        const users: PresenceUser[] = JSON.parse(message.body);

                        setOnlineUsers(users);
                    }
                );

                // Lets backend know client joined the board
                client.publish({
                    destination: `/app/boards/${boardId}/join`,
                    body: "",
                });
            },

            onStompError: (frame) => {
                console.error("STOMP error:", frame);
            },
        });

        client.activate();

        return () => {

            // When WebSocket disconnects
            if (client.connected) {
                client.publish({
                    destination: `/app/boards/${boardId}/leave`,
                    body: "",
                })
            }

            client.deactivate();
        };
    }, [boardId]);

    return onlineUsers;
}

export default useWebSocket;