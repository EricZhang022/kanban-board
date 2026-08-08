import { useEffect, useState } from "react";
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

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

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

    const initials = user ? `${user.firstName[0]}${user.lastName[0]}` : "";

    const handleFirstNameSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log("New first name:", firstName);

        // TODO: Send first name update to backend
    };

    const handleLastNameSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log("New last name:", lastName);

        // TODO: Send last name update to backend
    };

    const handleUsernameSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log("New username:", username);

        // TODO: Send username update to backend
    };

    const handleEmailSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        console.log("New email:", email);

        // TODO: Send email update to backend
    };

    const handlePasswordSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            alert("New passwords do not match.");
            
            // Warn the user passwords do not match
            return;
        }

        // TODO: Send password update to backend
        console.log({
            currentPassword,
            newPassword,
            confirmPassword,
        });
    };

    return (
        <div className="min-h-screen">
            <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center shadow-md">
                <h1 className = "font-bold text-xl tracking-wide text indig-400 mr-6">
                    Welcome back!
                </h1>
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