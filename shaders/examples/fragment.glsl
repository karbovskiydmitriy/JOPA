#version 430 core

layout(location = 0) out vec4 color;

uniform ivec2 screenSize;

void main()
{
	vec2 uv = vec2(gl_FragCoord.xy) / screenSize;
	color = vec4(uv, 0.0, 1.0);
}