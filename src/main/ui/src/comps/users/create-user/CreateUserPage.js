import React from "react";

import * as pageConstants from "../../../constants/page-constants";

import GenericPageHeader from "../../global/GenericPageHeader";
import LoadingView from "../../global/LoadingView";
import CreateUserNav from "./CreateUserNav";

const CreateUserPage = ({user, isLoading, logOff, changePage}) => {

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
                                <GenericPageHeader isLoading={isLoading}/>
                            </header>

                        </section>
                        <nav>
                            <CreateUserNav changePage={changePage}/>
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
export default CreateUserPage