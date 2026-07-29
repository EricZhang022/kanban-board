import { useState } from "react"
import { Link, NavLink, useNavigate } from "react-router"

export function inputs(label:string, type:string, name:string, value:string, errorMessage: string, 
    placeholder: string, setVal: React.Dispatch<React.SetStateAction<string>>){
    return(
        <div>
            <label htmlFor={name} className="mb-2 block text-sm font-medium text-gray-700">{label}</label>
            <input type={type} name={name} id={name} value={value} placeholder={placeholder} 
            onChange={(event) => setVal(event.target.value)} className="w-full rounded-lg border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"/>

            {errorMessage && 
            <div>
                {errorMessage}
            </div>}
        </div>
    )
}


export default function SignupPage(){
    const [firstName, setFirstName] = useState("")
    const [lastName, setLastName] = useState("")
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [password2, setPassword2] = useState("")
    

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const url = "http://localhost:8080"

        console.log("ye")
    }
    return(
        <div className="flex min-h-screen items-center justify-center bg-gray-100 px-4">
            <div className="w-full max-w-md rounded-2xl bg-white p-8 shadow-lg">
                <h1 className="text-center text-3xl">Sign up for an Account!</h1>

                <form onSubmit={handleSubmit} className="space-y-5 pt-2 pb-2">
                {/* label, type, name, value, errormes, placeholder, setval */}
                    {inputs("First Name", "text", "firstName", firstName, "", "", setFirstName)}
                    {inputs("Last Name", "text", "lastName", lastName, "", "", setLastName)}
                    {inputs("Username", "text", "username", username, "", "", setUsername)}
                    {inputs("Email", "email", "email", email, "", "", setEmail)}
                    {inputs("Password", "password", "password", password, "", "", setPassword)}
                    {inputs("Re-enter Password", "password", "password2", password2, "", "", setPassword2)}

                    <button type="submit" className="w-full rounded-lg bg-blue-600 py-3 font-semibold text-white transition hover:bg-blue-700">
                        Sign Up
                    </button>

                </form>
            </div>

        </div>
    )
}