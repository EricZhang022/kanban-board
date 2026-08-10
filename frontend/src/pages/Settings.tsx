import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router";

interface User {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
}

function Settings() {

    const navigate = useNavigate();

    const [user, setUser] = useState<User | null>(null);
    const [profileOpen, setProfileOpen] = useState(false);
    const profileRef = useRef<HTMLDivElement>(null);

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [accountMessage, setAccountMessage] = useState("");
    const [accountSuccess, setAccountSuccess] = useState(false);
    const [passwordMessage, setPasswordMessage] = useState("");
    const [passwordSuccess, setPasswordSuccess] = useState(false);

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
            const userData = data.data;

            setUser(userData);

            setFirstName(userData.firstName);
            setLastName(userData.lastName);
            setUsername(userData.username);
            setEmail(userData.email);
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

    const initials = user ? `${user.firstName[0]}${user.lastName[0]}` : "";

    const handleFirstNameSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        const res = await fetch("http://localhost:8080/api/users/me/first-name", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({
                firstName: firstName,
            }),
        });

        const data = await res.json();
        setAccountMessage(data.message);

        if (!res.ok) {
            setAccountSuccess(false);
            return;
        }

        setUser((prev) => {
            if (!prev) return prev;

            return {
                ...prev,
                firstName: firstName,
            };
        });

        setAccountSuccess(true);
    };

    const handleLastNameSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        const res = await fetch("http://localhost:8080/api/users/me/last-name", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({
                lastName: lastName,
            }),
        });

        const data = await res.json();
        setAccountMessage(data.message);

        if (!res.ok) {
            setAccountSuccess(false);
            return;
        } 

        setUser((prev) => {
            if (!prev) return prev;

            return {
                ...prev,
                lastName: lastName,
            };
        });

        setAccountSuccess(true);
    };

    const handleUsernameSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        const res = await fetch("http://localhost:8080/api/users/me/username", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({
                username: username,
            }),
        });

        const data = await res.json();
        setAccountMessage(data.message);

        if (!res.ok) {
            setAccountSuccess(false);
            return;
        } 

        setUser((prev) => {
            if (!prev) return prev;

            return {
                ...prev,
                username: username,
            };
        });

        setAccountSuccess(true);
    };

    const handleEmailSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        const res = await fetch("http://localhost:8080/api/users/me/email", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({
                email: email,
            }),
        });

        const data = await res.json();
        setAccountMessage(data.message);

        if (!res.ok) {
            setAccountSuccess(false);
            return;
        } 

        setUser((prev) => {
            if (!prev) return prev;

            return {
                ...prev,
                email: email,
            };
        });

        setAccountSuccess(true);
    };

    const handlePasswordSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (currentPassword === newPassword) {
            setPasswordMessage("New password cannot match your current password");
            setPasswordSuccess(false);
            return;
        }

        if (newPassword !== confirmPassword) {
            setPasswordMessage("New passwords do not match");
            setPasswordSuccess(false);
            return;
        }

        const res = await fetch("http://localhost:8080/api/users/me/password", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({
                currentPassword: currentPassword,
                newPassword: newPassword,
            }),
        });

        const data = await res.json();
        setPasswordMessage(data.message);

        if (!res.ok) {
            setPasswordSuccess(false);
            return;
        }
        
        setPasswordSuccess(true);
    };

    const handleLogout = async () => {
        await fetch("http://localhost:8080/api/auth/logout", {
            method: "POST",
            credentials: "include",
        })

        navigate("/");
    }

    return (
        <div className="min-h-screen">
            <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center shadow-md">
                <button
                    onClick={() => navigate("/dashboard")}
                    className="font-bold text-xl tracking-wide text-indigo-400 hover:text-indigo-300 transition cursor-pointer"
                >
                    Dashboard
                </button>

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
                                onClick={handleLogout}
                                className="w-full text-left px-4 py-2 hover:bg-gray-100 transition cursor-pointer"
                            >
                                Sign Out
                            </button>

                        </div>
                    )}
                </div>

            </nav>

             {/* Settings Content */}
            <main className="max-w-3xl mx-auto px-6 py-10">

                {/* Profile Header */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6 flex items-center">

                    {/* Profile Circle */}
                    <div className="w-20 h-20 rounded-full bg-cyan-500 text-white flex items-center justify-center text-2xl font-bold">
                        {initials}
                    </div>

                    {/* Name + Username */}
                    <div className="ml-5">
                        <h1 className="text-2xl font-bold text-gray-800">
                            {user?.firstName} {user?.lastName}
                        </h1>

                        <p className="text-gray-500 mt-1">
                            @{user?.username}
                        </p>
                    </div>

                </div>

                {/* Account Information */}
                <div className="bg-white rounded-lg shadow-md p-6 mb-6">

                    <h2 className="text-xl font-bold text-gray-800 mb-6">
                        Account Information
                    </h2>

                    {/* First Name */}
                    <form
                        onSubmit={handleFirstNameSubmit}
                        className="mb-6"
                    >
                        <label
                            htmlFor="firstName"
                            className="block text-sm font-medium text-gray-700 mb-1"
                        >
                            First Name
                        </label>

                        <div className="flex gap-3">
                            <input
                                id="firstName"
                                type="text"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />

                            <button
                                type="submit"
                                className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                            >
                                Save
                            </button>
                        </div>
                    </form>

                    {/* Last Name */}
                    <form
                        onSubmit={handleLastNameSubmit}
                        className="mb-6"
                    >
                        <label
                            htmlFor="lastName"
                            className="block text-sm font-medium text-gray-700 mb-1"
                        >
                            Last Name
                        </label>

                        <div className="flex gap-3">
                            <input
                                id="lastName"
                                type="text"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />

                            <button
                                type="submit"
                                className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                            >
                                Save
                            </button>
                        </div>
                    </form>

                    {/* Username */}
                    <form
                        onSubmit={handleUsernameSubmit}
                        className="mb-6"
                    >
                        <label
                            htmlFor="username"
                            className="block text-sm font-medium text-gray-700 mb-1"
                        >
                            Username
                        </label>

                        <div className="flex gap-3">
                            <input
                                id="username"
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />

                            <button
                                type="submit"
                                className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                            >
                                Save
                            </button>
                        </div>
                    </form>

                    {/* Email */}
                    <form onSubmit={handleEmailSubmit}>
                        <label
                            htmlFor="email"
                            className="block text-sm font-medium text-gray-700 mb-1"
                        >
                            Email
                        </label>

                        <div className="flex gap-3">
                            <input
                                id="email"
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="flex-1 border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />

                            <button
                                type="submit"
                                className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                            >
                                Save
                            </button>
                        </div>
                    </form>

                    {accountMessage && (
                        <p className={accountSuccess ? "text-green-500 text-md mt-6" :"text-red-500 text-md mt-6"}>
                        {accountMessage}
                        </p>
                    )}


                </div>

                {/* Change Password */}
                <div className="bg-white rounded-lg shadow-md p-6">

                    <h2 className="text-xl font-bold text-gray-800 mb-8">
                        Change Password
                    </h2>

                    <form onSubmit={handlePasswordSubmit}>

                        {/* Current Password */}
                        <div className="mb-4">
                            <label
                                htmlFor="currentPassword"
                                className="block text-sm font-medium text-gray-700 mb-1"
                            >
                                Current Password
                            </label>

                            <input
                                id="currentPassword"
                                type="password"
                                value={currentPassword}
                                onChange={(e) => setCurrentPassword(e.target.value)}
                                className="w-full border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />
                        </div>

                        {/* New Password */}
                        <div className="mb-4">
                            <label
                                htmlFor="newPassword"
                                className="block text-sm font-medium text-gray-700 mb-1"
                            >
                                New Password
                            </label>

                            <input
                                id="newPassword"
                                type="password"
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                className="w-full border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />
                        </div>

                        {/* Confirm Password */}
                        <div className="mb-6">
                            <label
                                htmlFor="confirmPassword"
                                className="block text-sm font-medium text-gray-700 mb-1"
                            >
                                Confirm New Password
                            </label>

                            <input
                                id="confirmPassword"
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                className="w-full border border-gray-300 rounded-md px-3 py-2 text-gray-800 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                required
                            />
                        </div>

                        {passwordMessage && (
                            <p className={passwordSuccess ? "text-green-500 text-md my-6" :"text-red-500 text-md my-6"}>
                            {passwordMessage}
                            </p>
                        )}

                        {/* Change Password Button */}
                        <button
                            type="submit"
                            className="bg-cyan-500 text-white px-5 py-2 rounded-md font-medium hover:bg-cyan-400 transition cursor-pointer"
                        >
                            Change Password
                        </button>
                    </form>
                </div>
            </main>
        </div>
    );
}

export default Settings