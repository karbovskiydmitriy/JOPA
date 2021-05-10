uniform ivec2 windowSize;

void main()
{
	vec2 uv = vec2(gl_FragCoord.xy) / vec2(windowSize);
	gl_FragColor = vec4(uv, 0.0, 1.0);
}
