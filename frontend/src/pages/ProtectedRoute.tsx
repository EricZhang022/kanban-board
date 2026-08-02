import { useEffect, useState } from "react";
import { Navigate } from "react-router";

function ProtectedRoute({ children }: { children: React.ReactNode }) {
    const [authenticated, setAuthenticated] = useState<boolean | null>(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/auth/check", {
            credentials: "include",
        }).then((res) => {
            setAuthenticated(res.ok);
        });
    }, []);

    if (authenticated === null) {
        return <div>Loading...</div>;
    }

    if (!authenticated) {
        return <Navigate to="/" replace />;
    }

    return <>{children}</>;
}

export default ProtectedRoute;