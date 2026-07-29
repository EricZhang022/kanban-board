import { useState } from "react"
import { Link, NavLink, useNavigate } from "react-router"

export function inputs(label:string, type:string, name:string, value:string, errorMessage: string, 
    placeholder: string, setVal: React.Dispatch<React.SetStateAction<string>>){
    return(
        <div>
            <label htmlFor={name}>{label}</label>
            <input type={type} name={name} id={name} value={value} placeholder={placeholder} onChange={(event) => setVal(event.target.value)}/>

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
    

    const handleSubmit = () => {
        
    }
    return(
        <div className="flex min-h-screen items-center justify-center bg-gray-100 px-4">
            <div>
                <h1>Sign up for an Account!</h1>

                <form action={handleSubmit}>
                {/* label, type, name, value, errormes, placeholder, setval */}
                    {inputs("firstName", "text", "firstName", firstName, "", "", setFirstName)}
                    {inputs("lastName", "text", "lastName", lastName, "", "", setLastName)}
                    {inputs("username", "text", "username", username, "", "", setUsername)}
                    {inputs("email", "email", "email", email, "", "", setEmail)}
                    {inputs("password", "password", "password", password, "", "", setPassword)}
                    {inputs("password2", "password", "password2", password2, "", "", setPassword2)}

            

                </form>
            </div>

        </div>
    )
}