import LoadingView from "../../global/LoadingView";
import GenericPageHeader from "../../global/GenericPageHeader";
import * as pageConstants from "../../../constants/page-constants";
import CreateUserNav from "./CreateUserNav";
import React from "react";

const CreateUserContainer = ({user, isLoading, logOff, changePage, onClickHandler}) => {

    return (
        <div className={"page createUserPage"}>
            <LoadingView isLoading={isLoading} message={"Loading.."}/>
            {!isLoading && (
                <div>
                    <header>
                        <h2>PiCenter Users Page</h2>

                    </header>
                    <div id="main">
                        <section>
                            <header>
                                <GenericPageHeader isLoading={isLoading} currentTabIndex={0} onClickHandler={onClickHandler} tabs={pageConstants.USERS_TABS}/>
                            </header>

                        </section>
                        <nav>
                            <CreateUserNav changePage={changePage} />
                        </nav>
                        <aside>
                            <h4>Logged on as: {user.userName}</h4>
                            <button type={"button"} onClick={logOff}>
                                Logout
                            </button>
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
export default CreateUserContainer