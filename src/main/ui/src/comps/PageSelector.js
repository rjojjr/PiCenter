import React from "react";

import * as constants from "../constants/page-constants";

import LoginPageContainer from './login/LogOnPageContainer';
import SummaryPageContainer from "./summary/SummaryPageContainer";

const PageSelector = ({user}) =>{
    const page = user.page;
    return(
        <div className={"pageSelector"}>
            {page === constants.LOGIN_PAGE && <LoginPageContainer />}
            {page === constants.SUMMARY_PAGE && <SummaryPageContainer />}
        </div>
    )
}
export default PageSelector