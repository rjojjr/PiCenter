import axios from 'axios';

const loadEndpoint = '/';

export const load = () =>
    axios.get(loadEndpoint,{ timeout: 10000 });
