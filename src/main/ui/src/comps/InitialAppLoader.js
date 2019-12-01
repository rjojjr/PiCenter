import React, { useEffect } from "react";
import { connect } from "react-redux";
import LoadingView from "../../oldSrc/LoadingView";

import { loadAppThunk } from "../actions/loader-actions";

import AppContainer from "./AppContainer";

const InitialAppLoader = ({ isError, loadApp, errorMsg, isLoading }) => {
  useEffect(() => {
    loadApp();
  }, [loadApp]);

  return (
    <div>
      <LoadingView isLoading={isLoading} message={"Loading..."}/>
      {/*//{isError && <p className="message">{errorMsg}</p>}*/}
      {/*{isShowMsg &&(
                <p className="message">{message}</p>
            )}*/}
      {!isLoading && <AppContainer />}
    </div>
  );
};

const mapStateToProps = state => ({
  //isError: state.isLoadingError,
  //errorMsg: state.loadingErrorMsg,
  isLoading: state.isLoading
});

const mapDispatchToProps = {
  loadApp: loadAppThunk
};
export default connect(mapStateToProps, mapDispatchToProps)(InitialAppLoader);
