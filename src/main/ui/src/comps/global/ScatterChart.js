import React, {PureComponent} from 'react';
import {
    Scatter, Line, XAxis, YAxis, ZAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const ScatterChart = ({data}) => {
    return (
        <ScatterChart width={730} height={250}
                      margin={{ top: 20, right: 20, bottom: 10, left: 10 }}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="x" name="stature" unit="cm" />
            <YAxis dataKey="y" name="weight" unit="kg" />
            <ZAxis dataKey="z" range={[64, 144]} name="score" unit="km" />
            <Tooltip cursor={{ strokeDasharray: '3 3' }} />
            <Legend />
            <Scatter name="A school" data={data01} fill="#8884d8" />
            <Scatter name="B school" data={data02} fill="#82ca9d" />
        </ScatterChart>
    );
};

export default ScatterChart;

export const CustomizedAxisTick = ({x, y, stroke, payload}) => {
    return (
        <g transform={`translate(${x},${y})`}>
            <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{payload.value}</text>
        </g>
    );
}