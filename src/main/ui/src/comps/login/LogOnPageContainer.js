import React, { useState } from "react";
import { connect } from "react-redux";

import { isLoggingOn, logOnThunk } from "../../actions/logon-actions";
import { resetIsShowMsg } from "../../actions/universal-actions";
import PageSelector from "../PageSelector";
import LogOnPage from "./LogOnPage";
import MainContent from "../layout/MainContent";
import MainContentHeader from "../layout/MainContentHeader";
import MainContentContainer from "../layout/MainContentConainer";
import LeftContentContainer from "../layout/LeftContentContainer";
import LeftContentHeader from "../layout/LeftContentHeader";
import LeftContent from "../layout/LeftContent";

const LogOnPageContainer = ({
  logOn,
  message,
  user,
  isShowMsg,
  resetIsShowMsg,
  isLoggingOn
}) => {
  return (

    <div className={"pageContainer logOnPage"}>

      <header><h2>PiCenter Logon</h2></header>
      <div id="main">
        <article>
          <LogOnPage
              user={user}
              logOn={logOn}
              resetIsShowMsg={resetIsShowMsg}
              isShowMsg={isShowMsg}
              message={message}
              isLoggingOn={isLoggingOn}
          />
        </article>
        <nav>Sign in</nav>
        <aside></aside>
      </div>
      <footer>Footer</footer>
      {/*<LeftContentContainer>
        <LeftContent>
          <h3>Sign In</h3>
        </LeftContent>
      </LeftContentContainer>
      <MainContentContainer>
        <MainContentHeader>
          <h2>PiCenter Logon</h2>
        </MainContentHeader>
        <MainContent >
          <LogOnPage
              user={user}
              logOn={logOn}
              resetIsShowMsg={resetIsShowMsg}
              isShowMsg={isShowMsg}
              message={message}
              isLoggingOn={isLoggingOn}
          />
        </MainContent>
      </MainContentContainer>*/}

    </div>
  );
};

const mapStateToProps = state => ({
  message: state.message,
  user: state.user,
  isShowMsg: state.isShowMsg,
  isLoggingOn: state.isLoggingOn
});

const mapDispatchToProps = {
  logOn: logOnThunk,
  resetIsShowMsg: resetIsShowMsg
};

export default connect(mapStateToProps, mapDispatchToProps)(LogOnPageContainer);
