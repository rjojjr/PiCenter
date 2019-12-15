import React, { PureComponent } from 'react';
import {
    AreaChart, Area, XAxis, YAxis, Tooltip, Legend, Line, LineChart,
} from 'recharts';

const DiffChart = ({data}) => {
    return(
        <AreaChart
            width={730}
            height={250}
            data={data}
            margin={{
                top: 20, right: 20, bottom: 20, left: 20,
            }}
        >
            <XAxis dataKey="day" />
            <YAxis />
            <Area name="Bedroom" dataKey="bedroom" stroke="#8884d8" fill="#8884d8" />
            <Area name="Living Room" dataKey="livingRoom" stroke="#82ca9d" fill="#82ca9d" />
            <Area name="Server Room" dataKey="serverRoom" stroke="#0c0707" fill="#0c0707" />
            <Area name="Office" dataKey="office" stroke="#fff633" fill="#fff633" />
            <Area name="Outside" dataKey="outside" stroke="#ff33ff" fill="#ff33ff" />
            <Area name="Heat On" dataKey="heat" stroke="#ff3f33" fill="#ff3f33" />
            <Tooltip />
        </AreaChart>
    );
};
export default DiffChart
