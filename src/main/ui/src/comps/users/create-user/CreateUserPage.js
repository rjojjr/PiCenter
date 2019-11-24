import React, {useState} from "react";

import * as pageConstants from "../../../constants/page-constants";

import GenericPageHeader from "../../global/GenericPageHeader";
import LoadingView from "../../global/LoadingView";
import CreateUserNav from "./CreateUserNav";

const CreateUserPage = ({user, isLoading, logOff, changePage, onClickHandler}) => {

    const [userName, setUserName] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [password, setPassword] = useState('');
    const [admin, setAdmin] = useState(false);

    const handleUserNameOnChange = (e) => {
        setUserName(e.target.value);
    }

    return (
        <div className={"page createUserPage"}>

        </div>
    )

}
export default CreateUserPage