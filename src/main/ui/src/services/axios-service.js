import axios from 'axios';

const loadEndpoint = '/';
const logOnEndpoint = '/logon';

export const load = () =>
    axios.get(loadEndpoint,{ timeout: 10000 });

export const logOn = (username, password) =>
    axios.post(logOnEndpoint, {username: username, password: password},{ timeout: 10000 });
