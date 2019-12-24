import React from "react";
import LoadingView from "../global/LoadingView";
import GenericPageHeader from "../global/GenericPageHeader";
import * as pageConstants from "../../constants/page-constants";
import Button from "react-bootstrap/Button";

const DeviceStatusContainer = ({user, changePage, isLoading, isError, errorMsg, isDeviceLoading, isDeviceError, getStatuses, restartPiTemp, restartDHT, logOff}) => {

    return(
        <div className={"pageContainer deviceStatusContainer"}>
            <LoadingView isLoading={isLoading}/>
            {!isLoading && (
                <div className={"pageContainer"}>
                    <div>
                        <header>
                            <h2>PiCenter Device Panel</h2>

                        </header>
                    </div>
                    <div id="main">
                        <section className={"deviceStatusPage"}>
                            <header className={"deviceStatusPage"}>
                                <GenericPageHeader isLoading={isLoading} currentTabIndex={0}
                                                   onClickHandler={() => {}} tabs={pageConstants.DEVICE_TABS}/>
                            </header>
                            <p>{errorMsg}</p>
                        </section>
                        <nav className={"deviceStatusPage"}>
                            <DevicePanelNav changePage={changePage}/>
                        </nav>
                        <aside className={"deviceStatusPage"}>
                            <h4>Logged on as: {user.userName}</h4>
                            <Button variant={"primary"} type={"button"} onClick={logOff}>
                                Logout
                            </Button>
                        </aside>
                    </div>
                    <div>
                        <footer>
                            <a className={"lightText"} href={"http://github.com/rjojjr"}>Visit me on github</a>
                        </footer>
                    </div>
                </div>
            )}
        </div>
    )

}
export default DeviceStatusContainer