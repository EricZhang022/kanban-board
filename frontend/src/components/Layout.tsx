import { useEffect, useRef, useState } from "react";
import { useNavigate, Outlet } from "react-router";

interface User {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
}

function Layout() {
    const navigate = useNavigate();
    const [user, setUser] = useState<User | null>(null);
    const [profileOpen, setProfileOpen] = useState(false);
    const profileRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const fetchUser = async () => {
            const res = await fetch("http://localhost:8080/api/users/me", {
                credentials: "include",
            });

            if (!res.ok) {
                navigate("/");
                return;
            }

            const data = await res.json();
            setUser(data.data);
        };

        fetchUser();
    }, [navigate]);

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
        });

        navigate("/");
    };

    const initials = user ? `${user.firstName[0]}${user.lastName[0]}` : "";

    return (
        <div className="min-h-screen">
            <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center shadow-md">
                <button
                    onClick={() => navigate("/dashboard")}
                    className="font-bold text-xl tracking-wide text-indigo-400 hover:text-indigo-300 transition cursor-pointer"
                >
                    Dashboard
                </button>

                <button
                    onClick={() => navigate("/dashboard")}
                    className="font-bold text-xl tracking-wide text-indigo-400 hover:text-indigo-300 transition cursor-pointer"
                >
                    Button 2
                </button>

                <button
                    onClick={() => navigate("/dashboard")}
                    className="font-bold text-xl tracking-wide text-indigo-400 hover:text-indigo-300 transition cursor-pointer"
                >
                    Button 3
                </button>

                <div ref={profileRef} className="relative">
                    <button
                        onClick={() => setProfileOpen(!profileOpen)}
                        className="w-10 h-10 rounded-full bg-cyan-500 flex items-center justify-center font-semibold hover:bg-cyan-400 transition cursor-pointer"
                    >
                        {initials}
                    </button>

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
                                onClick={() => {setProfileOpen(false); navigate("/settings")}}
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

            <main>
                <Outlet context={{ user, setUser }} />
            </main>
        </div>
    );
}

export default Layout;