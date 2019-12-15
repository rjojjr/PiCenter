import React, { PureComponent } from 'react';
import {
    AreaChart, Area, XAxis, YAxis, Tooltip, Legend, Line, LineChart,
} from 'recharts';

const DiffChart = ({data}) => {
    return(
        <AreaChart
            width={800}
            height={300}
            data={data}
            margin={{
                top: 5, right: 30, left: 20, bottom: 5,
            }}
        >
            <XAxis dataKey="name" />
            <YAxis />
            <Area name="Bedroom" dataKey="bedroom" stroke="#8884d8" fill="#8884d8" />
            <Area name="Living Room" dataKey="livingRoom" stroke="#82ca9d" fill="#82ca9d" />
            <Area name="Server Room" dataKey="serverRoom" stroke="#0c0707" fill="#0c0707" />
            <Area name="Office" dataKey="office" stroke="#fff633" fill="#fff633" />
            <Area name="Outside" dataKey="outside" stroke="#ff33ff" fill="#ff33ff" />
            <Area name="Heat On" dataKey="heat" stroke="#ff3f33" fill="#ff3f33" />
            <Tooltip />
            <Legend />
        </AreaChart>
    );
};
export default DiffChart

