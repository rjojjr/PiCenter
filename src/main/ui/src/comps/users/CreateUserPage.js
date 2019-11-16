import React from "react";

import {changePage} from "../../actions/universal-actions";
import * as pageConstants from "../../constants/page-constants";

import {logOff} from "../../actions/universal-actions";

const CreateUserPage = ({user}) => {
    
    return(
        <div>
            <header>
                <h2>PiCenter Users Page</h2>

            </header>
            <div id="main">
                <section>
                    <header>
                        
                    </header>
                    
                </section>
                <nav>
                    <ul>
                        <li>
                            <ul>
                                <li><b>Users</b>
                                    <ul>
                                        <li>
                                            <b>Create User</b>
                                        </li>
                                    </ul>
                                </li>
                                
                            </ul>
                            
                            <ul>
                                <li><a onClick={() => changePage(pageConstants.SUMMARY_PAGE)}>Summary</a></li>
                            </ul> 
                        </li>
                        
                    </ul>
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
    )
}