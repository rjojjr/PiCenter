import React from "react";

import{
    LOADING_PAGE,
    MSG_PAGE,
    LOGIN_PAGE,
    SUMMARY_PAGE
} from "../constants/page-constants";

import LoginPage from './login/LogOnPage';

const PageSelector = ({user}) =>{
    const page = user.page;
    return(
        <div>
            {page === LOGIN_PAGE && <LoginPage />}
        </div>
    )
}
export default PageSelector