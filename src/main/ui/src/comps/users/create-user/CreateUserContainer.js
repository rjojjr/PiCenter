import LoadingView from "../../global/LoadingView";
import GenericPageHeader from "../../global/GenericPageHeader";
import * as pageConstants from "../../../constants/page-constants";
import CreateUserNav from "./CreateUserNav";
import React from "react";
import CreateUserPage from "./CreateUserPage";
import Button from 'react-bootstrap/Button';

const CreateUserContainer = ({user, userMsg, isLoading, logOff, changePage, onClickHandler, resetUserMsg, createUser}) => {

    return (
        <div className={"pageContainer createUserPageContainer"}>
            <LoadingView isLoading={isLoading} message={"Loading.."}/>
            {!isLoading && (
                <div className={"pageContainer"}>
                    <header>
                        <h2>PiCenter Users Page</h2>

                    </header>
                    <div id="main">
                        <div className={"scrollPage"}>
                        <section className={"createUserPage"}>
                            <header className={"createUserPage"}>
                                <GenericPageHeader isLoading={isLoading} currentTabIndex={0} onClickHandler={onClickHandler} tabs={pageConstants.USERS_TABS}/>
                            </header>
                            <p>{userMsg}</p>
                            <CreateUserPage user={user} resetMsg={resetUserMsg} createUser={createUser}/>
                        </section>
                        </div>
                        <nav className={"createUserPage"}>
                            <CreateUserNav changePage={changePage} />
                        </nav>
                        <aside className={"createUserPage"}>
                            <h4>Logged on as: {user.userName}</h4>
                            <Button variant={"primary"} type={"button"} onClick={logOff}>
                                Logout
                            </Button>
                        </aside>
                    </div>
                    <footer>
                        <a className={"lightText"} href={"http://github.com/rjojjr"}>Visit me on github</a>
                    </footer>
                </div>
            )}
        </div>
    )

}
export default CreateUserContainer