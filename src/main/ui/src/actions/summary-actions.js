import { loadSummary } from "../services/axios-service";
import { setUser } from "./universal-actions";
import * as debugConstants from "../constants/debug-constants";

export const SUMMARY_LOADING_ERROR = "SUMMARY_LOADING_ERROR";
export const summaryLoadingError = msg => ({
  type: SUMMARY_LOADING_ERROR,
  msg
});

export const SUMMARY_LOADING = "SUMMARY_LOADING";
export const summaryLoading = () => ({
  type: SUMMARY_LOADING
});

export const SET_SUMMARY = "SET_SUMMARY";
export const setSummary = summary => ({
  type: SET_SUMMARY,
  summary
});

export const loadSummaryThunk = user => async dispatch => {
  try {
    dispatch(summaryLoading());
    const summaryResponse = await loadSummary(user);
    if (summaryResponse.data.body === "error") {
      setUser({}, false);
      return;
    }
    const restUser = summaryResponse.data.restUser;
    setUser(restUser, true);
    setSummary(summaryResponse.data.summary);
  } catch (error) {
    if (
      process.env.NODE_ENV === "development" &&
      debugConstants.ALERT_DEBUG_THUNKS
    ) {
      alert(error);
    }
    dispatch(summaryLoadingError("Network error"));
  }
};
