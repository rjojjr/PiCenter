import React from "react";

import * as constants from "../constants/page-constants";

import LoginPageContainer from './login/LogOnPageContainer';
import SummaryPageLoader from "./summary/SummaryPageLoader";

const PageSelector = ({user}) =>{
    const page = user.page;
    return(
        <div className={"pageSelector"}>
            {page === constants.LOGIN_PAGE && <LoginPageContainer />}
            {page === constants.SUMMARY_PAGE && <SummaryPageLoader />}
        </div>
    )
}
export default PageSelector