#version 430 core

layout(location = 0) out vec4 color;

void main()
{
	vec2 uv = vec2(gl_FragCoord.xy) / vec2(1280, 1024);
	color = vec4(uv, 0.0, 1.0);
}