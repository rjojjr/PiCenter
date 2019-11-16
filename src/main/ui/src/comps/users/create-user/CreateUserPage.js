import React from "react";

import {changePage} from "../../../actions/universal-actions";
import * as pageConstants from "../../../constants/page-constants";

import {logOff} from "../../../actions/universal-actions";
import GenericPageHeader from "../../global/GenericPageHeader";
import LoadingView from "../../global/LoadingView";

const CreateUserPage = ({user, isLoading}) => {

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

                        </nav>
                        <aside>
                            <h4>Logged on as: {user.userName}</h4>
                            <button type={"button"} onClick={logOff}>
                                Logout
                            </button>
                        </aside>
                    </div>
                    <footer>
                        <a href={"github.com/rjojjr"}>Visit me on github</a>
                    </footer>
                </div>
            )}
        </div>
    )

}
export default CreateUserPage