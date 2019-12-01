import React, {useState} from "react";

import LoadingView from "../global/LoadingView";
import {loadSummaryThunk} from "../../actions/summary-actions";
import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";

const DataPageContainer = ({user, changePage}) => {
    
};

const mapStateToProps = state => ({
    user: state.user,
});

const mapDispatchToProps = {
    logOff,
    changePage
};

export default connect(mapStateToProps, mapDispatchToProps)(DataPageContainer);