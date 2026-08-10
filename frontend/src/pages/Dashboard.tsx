import { useEffect, useRef, useState } from "react";
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
    const [profileOpen, setProfileOpen] = useState(false);
    const profileRef = useRef<HTMLDivElement>(null);

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

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (
                profileRef.current &&
                !profileRef.current.contains(event.target as Node)
            ) {
                setProfileOpen(false);
            }
        };

        document.addEventListener("mousedown", handleClickOutside);

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const handleLogout = async () => {
        await fetch("http://localhost:8080/api/auth/logout", {
            method: "POST",
            credentials: "include",
        })

        navigate("/");
    }

    const initials = user ? `${user.firstName[0]}${user.lastName[0]}` : "";

    return (
        <div>
            <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center shadow-md">
            
                <h1 className = "font-bold text-xl tracking-wide text indig-400 mr-6">
                    Welcome back, {user?.firstName}!
                </h1>

                <div ref={profileRef} className="relative">

                    {/* Profile Avatar */}
                    <button
                        onClick={() => setProfileOpen(!profileOpen)}
                        className="w-10 h-10 rounded-full bg-cyan-500 flex items-center justify-center font-semibold hover:bg-cyan-400 transition cursor-pointer"
                    >
                        {initials}
                    </button>

                    {/* Profile Dropdown */}
                    {profileOpen && (
                        <div className="absolute right-0 mt-2 w-48 bg-white text-gray-800 rounded-md shadow-lg overflow-hidden">

                            <div className="px-4 py-3 border-b">
                                <p className="font-semibold">
                                    {user?.firstName} {user?.lastName}
                                </p>

                                <p className="text-sm text-gray-500">
                                    @{user?.username}
                                </p>
                            </div>

                            <button
                                onClick={() => navigate("/settings")}
                                className="w-full text-left px-4 py-2 hover:bg-gray-100 transition cursor-pointer"
                            >
                                Settings
                            </button>

                            <button
                                onClick={handleLogout}
                                className="w-full text-left px-4 py-2 hover:bg-gray-100 transition cursor-pointer"
                            >
                                Sign Out
                            </button>

                        </div>
                    )}
                </div>
            </nav>
        </div>
    );
}

export default Dashboard