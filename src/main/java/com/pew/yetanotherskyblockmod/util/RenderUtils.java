// package com.pew.yetanotherskyblockmod.util;

// public class RenderUtils {
//     public void boxSides(double x1, double y1, double z1, double x2, double y2, double z2, Color color, int excludeDir) {
//         int blb = triangles.vec3(x1, y1, z1).color(color).next();
//         int blf = triangles.vec3(x1, y1, z2).color(color).next();
//         int brb = triangles.vec3(x2, y1, z1).color(color).next();
//         int brf = triangles.vec3(x2, y1, z2).color(color).next();
//         int tlb = triangles.vec3(x1, y2, z1).color(color).next();
//         int tlf = triangles.vec3(x1, y2, z2).color(color).next();
//         int trb = triangles.vec3(x2, y2, z1).color(color).next();
//         int trf = triangles.vec3(x2, y2, z2).color(color).next();

//         if (excludeDir == 0) {
//             // Bottom to top
//             triangles.quad(blb, blf, tlf, tlb);
//             triangles.quad(brb, trb, trf, brf);
//             triangles.quad(blb, tlb, trb, brb);
//             triangles.quad(blf, brf, trf, tlf);

//             // Bottom
//             triangles.quad(blb, brb, brf, blf);

//             // Top
//             triangles.quad(tlb, tlf, trf, trb);
//         }
//         else {
//             // Bottom to top
//             if (Dir.isNot(excludeDir, Dir.WEST)) triangles.quad(blb, blf, tlf, tlb);
//             if (Dir.isNot(excludeDir, Dir.EAST)) triangles.quad(brb, trb, trf, brf);
//             if (Dir.isNot(excludeDir, Dir.NORTH)) triangles.quad(blb, tlb, trb, brb);
//             if (Dir.isNot(excludeDir, Dir.SOUTH)) triangles.quad(blf, brf, trf, tlf);

//             // Bottom
//             if (Dir.isNot(excludeDir, Dir.DOWN)) triangles.quad(blb, brb, brf, blf);

//             // Top
//             if (Dir.isNot(excludeDir, Dir.UP)) triangles.quad(tlb, tlf, trf, trb);
//         }

//         triangles.growIfNeeded();
//     }
// }
