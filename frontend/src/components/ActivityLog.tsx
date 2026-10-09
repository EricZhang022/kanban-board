import { useState, useEffect } from "react"

export interface LogEntry {
    logID: string;
    summary: string;
    editorName: string;
    createdAt: string;
}

export default function ActivityLog({boardId}: {boardId: string}){
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const [logs, setLogs] = useState<LogEntry[]>([])

    const [currentPage, setCurrentPage] = useState(1);



    async function fetchActivityLog(boardId: string){
        const res = await fetch(`http://localhost:8080/api/activitylog/${boardId}`, {
            credentials: "include",
        });
        if (!res.ok) {
            throw new Error("Failed to load activity log");
        }
        const body = await res.json();
        return body.data;
}

    useEffect(() => {
        const load = async () => {
            setLoading(true);
            setError(false);
            try {
                const data = await fetchActivityLog(boardId);
                setLogs(data);
            } catch {
                setError(true);
            } finally {
                setLoading(false);
            }
        };
        load();
    }, [boardId]);
    return(
        <div className="border border-gray-200 rounded-lg p-6 mb-6">
            {loading ? (
                <p className="text-sm text-gray-500">Loading...</p>
            ) : error ? (
                <p className="text-sm text-red-500">Couldn't load activity log.</p>
            ) : logs.length === 0 ? (
                <p className="text-sm text-gray-500">No activity yet.</p>
            ) : (
                <ul className="space-y-2">
                    {logs.map((log) => (
                        <li key={log.logID} className="text-sm text-gray-700">
                            <span className="font-medium">{log.editorName}: </span> {log.summary}
                            <span className="text-xs text-gray-400 ml-2">
                                {new Date(log.createdAt).toLocaleString()}
                            </span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}