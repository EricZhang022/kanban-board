import { useNavigate } from "react-router";
function HomePage() {
    const navigate = useNavigate();

        const testSecure = async () => {
        const response = await fetch("http://localhost:8080/secure", {
            credentials: "include",
        });
        const text = await response.text();
        console.log("Secure response:", text, response.status);
    };

    return (
        <div>
{/* -------------------------------------------NAV--------------------------------------------------------------------------------*/}
            <nav className="bg-gray-800 text-white px-6 py-4 flex items-center shadow-md">
                <h1 className = "font-bold text-xl tracking-wide text indig-400 mr-6" onClick={() => navigate('/login')}>
                    Personal Kanboard Project
                </h1>

                <div className = "flex items-center space-x-2">
                    <a href = "#" className="bg-gray-900 px-4 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition">
                        Home
                    </a>

                    <a href = "#" className = "px-4 py-2 rounded-md text-sm font-medium text-gray-300 hover:bg-gray-700 hover:text-white transition">
                        Something Else????
                    </a>
{/*                 # test purpose
                    <button onClick={testSecure} className="px-4 py-2 rounded-md text-sm font-medium bg-green-600 hover:bg-green-700 transition">
                        Test /secure
                    </button> */}
                </div>
            </nav>
{/* -------------------------------------------------------------------------------------------------------------------------------*/}
        </div>
    );
}

export default HomePage