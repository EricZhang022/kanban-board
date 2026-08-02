import { Link, NavLink, useNavigate } from "react-router"
import { useEffect } from "react";

function HomePage() {
    const navigate = useNavigate();

    useEffect(() => {
        fetch("http://localhost:8080/api/auth/check", {
            credentials: "include",
        })
        .then((res) => {
            if (res.ok) {
                navigate("/dashboard");
            }
        });
    }, []);

    return (
        <div>
{/* -------------------------------------------NAV--------------------------------------------------------------------------------*/}
            <nav className="bg-gray-800 text-white px-6 py-4 flex items-center shadow-md">
                <h1 className = "font-bold text-xl tracking-wide text indig-400 mr-6">
                    Personal Kanboard Project
                </h1>

                <div className = "ml-auto flex items-center space-x-2">
                    <a href = "#features" className="px-4 py-2 rounded-md text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition">
                        Features
                    </a>

                    <a href = "#" className="px-4 py-2 rounded-md text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition">
                        About Us
                    </a>

                    <a href = "/login" className = "px-4 py-2 rounded-md text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition">
                        Sign in
                    </a>
                </div>
            </nav>
{/* -------------------------------------------------------------------------------------------------------------------------------*/}
            <main className="flex-1">
                <section className="max-w-5xl mx-auto px-6 py-20 text-center">
                    <h2 className="text-4xl md:text-5xl font-extrabold mt-4 mb-6 tracking-tight">
                        Make Tasks.
                    </h2>
                    <p className="text-lg text-slate-400 max-w-2xl mx-auto mb-8 leading-relaxed">
                        That's Right! You're not dealing with the average Kanboard anymore . . .  
                    </p>
                    <div className="flex justify-center gap-4">
                        <a href="/login" className="bg-indigo-600 hover:bg-indigo-500 text-white font-semibold px-6 py-3 rounded-lg shadow-lg transition">
                            Start Kanboarding!
                        </a>
                    </div>
                </section>

                <section id="features" className="bg-gray-800 border-t border-slate-800 max-w-6xl mx-auto px-6 py-16 shadow-md mb-10">
                    <div className="text-center mb-12">
                        <h3 className="text-2xl font-bold text-white" >Features</h3>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div className="bg-[#1a2332] p-6 rounded-xl border border-slate-800">
                            <h3 className="text-lg font-semibold text-white mb-2">Banana1</h3>
                            <p className="text-slate-400 text-sm">placeholder</p>
                        </div>

                        <div className="bg-[#1a2332] p-6 rounded-xl border border-slate-800">
                            <h3 className="text-lg font-semibold text-white mb-2">Banana2</h3>
                            <p className="text-slate-400 text-sm">placeholder</p>
                        </div>

                        <div className="bg-[#1a2332] p-6 rounded-xl border border-slate-800">
                            <h3 className="text-lg font-semibold text-white mb-2">Banana3</h3>
                            <p className="text-slate-400 text-sm">placeholder</p>
                        </div>
                    </div>
                </section>
            </main>

            <footer className="bg-gray-800 border-t border-slate-800 py-6 px-6 text-center text-xs text-slate-400">
                <div className="max-w-6xl mx-auto flex flex-col md:flex-row justify-between items-center">
                    <div className="flex gap-4 font-medium">
                        <a className="hover:text-slate-400 transition cursor-pointer">No Help</a>
                        <a className="hover:text-slate-400 transition cursor-pointer">No Privacy</a>
                        <a className="hover:text-slate-400 transition cursor-pointer">No Terms</a>
                    </div>

                    <p> {new Date().getFullYear()} © Personal Kanboard Project. All rights reserved. All lefts reserved. Bungee Gum possesses the properties of both rubber and gum.</p>
                </div>
            </footer>

        </div>
    );
}

export default HomePage