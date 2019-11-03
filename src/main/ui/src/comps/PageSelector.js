import React from "react";

import * as constants from "../constants/page-constants";

import LoginPageContainer from './login/LogOnPageContainer';

const PageSelector = ({user}) =>{
    const page = user.page;
    return(
        <div className={"pageSelector"}>
            {page === constants.LOGIN_PAGE && <LoginPageContainer />}
            {page === constants.SUMMARY_PAGE && <LoginPage />}
        </div>
    )
}
export default PageSelector