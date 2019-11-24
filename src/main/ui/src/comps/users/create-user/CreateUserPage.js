import React, {useState} from "react";

import * as pageConstants from "../../../constants/page-constants";

import GenericPageHeader from "../../global/GenericPageHeader";
import LoadingView from "../../global/LoadingView";
import CreateUserNav from "./CreateUserNav";

const CreateUserPage = ({user, isLoading, logOff, changePage, onClickHandler, resetMsg, createUser}) => {

    const [userName, setUserName] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [password, setPassword] = useState('');
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

    const handlePasswordNameOnChange = (e) => {
        setPassword(e.target.value);
        resetMsg();
    }

    const handleAdminOnChange = (e) => {
        const temp = e.target.value;
        if(temp.toLowerCase().includes('t')){
            setAdmin(true);
        }else{
            setAdmin(false);
        }
        resetMsg();
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
                                <input type={"text"} value={password} onChange={handleLastNameOnChange}/>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    )

}
export default CreateUserPage