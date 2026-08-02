import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

interface User {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
}

function Dashboard() {

    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            const res = await fetch("http://localhost:8080/api/users/me", {
                credentials: "include",
            });
        
            const data = await res.json();
            setUser(data.data);
        };

        fetchUser();        
    }, []);

    const handleLogout = async () => {
        await fetch("http://localhost:8080/api/auth/logout", {
            method: "POST",
            credentials: "include",
        })

        navigate("/");
    }

    return (
        <div>
            <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center shadow-md">
            
                <h1 className = "font-bold text-xl tracking-wide text indig-400 mr-6">
                    Welcome back, {user?.firstName}!
                </h1>

                <button className="bg-gray-900 px-4 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition" onClick={handleLogout}>
                    Sign Out
                </button>
            </nav>
        </div>
    );
}

export default Dashboard