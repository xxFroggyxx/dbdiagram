export const Polkadot = () => {
	return (
		<svg
			className='bg-white dark:bg-black'
			style={{
				position: 'absolute',
				width: '100%',
				height: '100%',
				top: 0,
				left: 0,
				zIndex: -1,
			}}
		>
			<pattern
				id='pattern-heroundefined'
				x='10'
				y='0'
				width='20'
				height='20'
				patternUnits='userSpaceOnUse'
				patternTransform='translate(-0.5,-0.5)'
			>
				<circle cx='0.5' cy='0.5' r='0.5' fill='#64748b'></circle>
			</pattern>
			<rect
				x='0'
				y='0'
				width='100%'
				height='100%'
				fill='url(#pattern-heroundefined)'
			></rect>
		</svg>
	);
};
