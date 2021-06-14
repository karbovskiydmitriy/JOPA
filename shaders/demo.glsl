#version 430 core

#define PI 3.141592653589793238462643383279

layout(local_size_x = 4) in;

struct Star
{
	float angle;
	float dist;
};

layout(rgba32f, binding = 0) uniform image2D image;
layout(std430, binding = 1) buffer entities
{
	Star stars[];
};

uniform int operation;
uniform int count;
uniform float deltaTime;

ivec2 size;

const float spawnDistance = 0.18;
const float speed = 2.25;
const float blurSpeed = 0.22;
const float fadeDistance = 0.3;
const vec4 dotColor = vec4(0.18, 0.72, 0.81, 1.0);

float random(float f)
{
	return fract(sin(f) * 43758.5453123);
}

void init(int id, bool first)
{
	stars[id].angle = random(id + count * deltaTime) * 2 * PI;

	stars[id].dist = random(count - id + deltaTime) * (first ? 1 : spawnDistance);
}

ivec2 getPosition(int index)
{
	float angle = stars[index].angle;
	float dist = stars[index].dist;
	return size / 2 + ivec2(ivec2(size.x / 2 / (size.x / size.y), size.y / 2) * vec2(cos(angle), sin(angle)) * dist);
}

void main(void)
{
	ivec2 id = ivec2(gl_GlobalInvocationID.xy);
	size = imageSize(image);

	switch (operation) {
	case 1:
		for (int i = 0; i < count; i++)
		{
			init(i, true);
		}
		break;
	case 2:
		int index = id.x;
		float dist = stars[index].dist;
		dist += dist * dist * speed * deltaTime;
		stars[index].dist = dist;
		ivec2 pos = getPosition(index);
		if (pos.x > size.x || pos.y > size.y || pos.x < 0 || pos.y < 0)
		{
			init(index, false);
			pos = getPosition(index);
		}
		vec4 color = dotColor;
		if (dist < fadeDistance)
		{
			color = color * dist / fadeDistance;
		}
		imageStore(image, ivec2(pos), color);
		break;
	case 3:
		for (int i = size.y / 32 * id.y; i < size.y / 32 * (id.y + 1); i++)
		{
			for (int j = size.x / 32 * id.x; j < size.x / 32 * (id.x + 1); j++)
			{
				ivec2 coord = ivec2(j, i);
				vec3 color = imageLoad(image, coord).xyz;
				color *= (1 - blurSpeed);
				imageStore(image, coord, vec4(color, 1));
			}
		}
		break;
	}
}