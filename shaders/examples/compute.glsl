#version 430 core

layout(local_size_x = 2, local_size_y = 2) in;

layout(rgba32f, binding = 0, location = 0) uniform image2D image;

void main()
{
	vec2 uv = vec2(gl_GlobalInvocationID.xy) / imageSize(image);
	imageStore(image, ivec2(gl_GlobalInvocationID.xy), vec4(uv, 0.0, 1.0));
}