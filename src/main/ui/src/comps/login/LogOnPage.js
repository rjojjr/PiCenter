import React, {useState} from 'react';

import PageSelector from "../PageSelector";

const LogOnPage = ({logOn, message, user, isShowMsg, resetIsShowMsg, isLoggingOn}) => {

    const[username, setUsername] = useState('');
    const[password, setPassword] = useState('');

    const handleUsernameOnChange = (e) => {
        setUsername(e.target.value);
        resetIsShowMsg();
    }

    const handlePasswordOnChange = (e) => {
        setPassword(e.target.value);
        resetIsShowMsg()
    }

    const handleOnClick = () => {
        logOn(username, password)
    }

    return (
        //Put header here
        <div className={"logOnForm"}>
            <h2>PiCenter Logon</h2>
            {isLoggingOn && <p className={"loading"}>Logging on....</p>}
            {user.username !== undefined && <PageSelector user={user}/>}
            <p className={"message"}>{message}</p>
            <table>
                <tbody>
                <tr>
                    <td>
                        <p>Username: </p>
                    </td>
                    <td>
                        <input type={"text"} className={"logonInput"} onChange={handleUsernameOnChange} value={username}/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <p>Password: </p>
                    </td>
                    <td>
                        <input type={"password"} className={"logonInput"} onChange={handlePasswordOnChange} value={password}/>
                    </td>
                </tr>
                </tbody>
            </table>
            <button className={"submit"} type={"button"} onClick={handleOnClick}>Logon</button>
        </div>
        //Put footer here
    )
};

export default LogOnPage;