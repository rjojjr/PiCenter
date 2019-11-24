import React, {useState} from "react";

const CreateUserPage = ({user, isLoading, logOff, changePage, onClickHandler, resetMsg, createUser}) => {

    const [userName, setUserName] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [password, setPassword] = useState('');
    const [adminText, setAdminText] = useState('False');
    const [admin, setAdmin] = useState(false);

    const handleUserNameOnChange = (e) => {
        setUserName(e.target.value);
        resetMsg();
    }

    const handleFirstNameOnChange = (e) => {
        setFirstName(e.target.value);
        resetMsg();
    }

    const handleLastNameOnChange = (e) => {
        setLastName(e.target.value);
        resetMsg();
    }

    const handlePasswordOnChange = (e) => {
        setPassword(e.target.value);
        resetMsg();
    }

    const handleAdminOnChange = () => {
        if(admin){
            setAdmin(true);
            setAdminText('true');
        }else{
            setAdmin(false);
            setAdminText('false');
        }
        resetMsg();
    }

    const handleOnSubmit = () => {
        createUser(userName, firstName, lastName, password, admin);
    }

    return (
        <div className={"page createUserPage"}>
            <form>
                <table>
                    <tbody>
                        <tr>
                            <td>
                               Username
                            </td>
                            <td>
                                <input type={"text"} value={userName} onChange={handleUserNameOnChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                First Name
                            </td>
                            <td>
                                <input type={"text"} value={firstName} onChange={handleFirstNameOnChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Last Name
                            </td>
                            <td>
                                <input type={"text"} value={lastName} onChange={handleLastNameOnChange}/>
                            </td>

                        </tr>
                        <tr>
                            <td>
                                Password
                            </td>
                            <td>
                                <input type={"text"} value={password} onChange={handlePasswordOnChange}/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                Admin
                            </td>
                            <td>
                                {adminText}
                            </td>
                            <td>
                                <button onClick={handleAdminOnChange}>Change</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <button onClick={handleOnSubmit}>Create User</button>
            </form>
        </div>
    )

}
export default CreateUserPage