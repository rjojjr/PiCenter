import LoadingView from "../../global/LoadingView";
import GenericPageHeader from "../../global/GenericPageHeader";
import CSVPageNav from "./CSVPageNav";
import * as pageConstants from "../../../constants/page-constants";
import React from "react";
import Button from 'react-bootstrap/Button';
import CSVPage from "./CSVPage";
import DownloadAlert from "./DownloadAlert";

const CSVPageContainer = ({user, changePage, updateSession, isLoading, isError, errorMsg, isDataError, logOff, onClickHandler, getCSV, isDownload, isDownloadAvailable}) => {
    
    updateSession(pageConstants.CSV_EXPORT, user);

    return (
        <div className={"pageContainer csvPageContainer"}>
            <LoadingView isLoading={isLoading}/>
            {!isLoading && (
                <div>
                    <header>
                        <h2>PiCenter Data Page</h2>

                    </header>
                    <div id="main">
                        <section className={"csvPage"}>
                            <header className={"csvPage"}>
                                <GenericPageHeader isLoading={isLoading} currentTabIndex={0} onClickHandler={onClickHandler} tabs={pageConstants.DATA_TABS}/>
                            </header>
                            <p>{errorMsg}</p>
                            <DownloadAlert show={isDownloadAvailable} isDownload={isDownload}/>
                            <CSVPage user={user} isDataError={isDataError} getCSV={getCSV}/>
                        </section>
                        <nav className={"csvPage"}>
                            <CSVPageNav changePage={changePage} />
                        </nav>
                        <aside className={"csvPage"}>
                            <h4>Logged on as: {user.userName}</h4>
                            <Button variant={"primary"} type={"button"} onClick={logOff}>
                                Logout
                            </Button>
                        </aside>
                    </div>
                    <footer>
                        <a href={"http://github.com/rjojjr"}>Visit me on github</a>
                    </footer>
                </div>
            )}
        </div>
    )

}
export default CSVPageContainer