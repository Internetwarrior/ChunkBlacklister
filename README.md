# Redstone Chunk Blacklister

## Briefing

I was tasked with the following:

You have a world full of piston contraptions, and you want to limit those contraptions so that if a chunk contains > x (say, 50) pistons, it gets blacklisted and no redstone updates can happen in that chunk until the number of pistons goes below that amount again. 

You can’t simply track pistons in a chunk by listening to the block place event, as you know that the world already exists and many pistons could already exist within the chunk. 

You need to come up with a way to count all the pistons within the chunk at a relevant time, without it causing any performance decrease to the server.

## Technical Breakdown

### Inital Understanding
My first thought was that I obviously needed to count the amount of pistons when a Chunk has been loaded and cache this number. Everytime a piston was broken or place from that point, I'd be able to change this stored number to the corresponding Chunk.

I knew that each block's data is iterated through the default NMS, I found this code to be within the class of Chunk, Chunk actually splits it's loading down into multiple ChunkSections which then iterates through a number of blocks which I could count. The only way to take advantage of this is to make my own edition of Spigot, which would mean that this project could break at any point in the future and would require a large amount of development time to upkeep. However, this would have made literally no performance impact, keeping O(n) performance.

### Final solution
My next thought was to simplify the issue, time is expensive and as a result, It may make sense to go with a more simple approach. I need to count how many times a block's id (a piston's) occurs in a specific chunk. The block data which is used to load the chunks which i previously covered could actually be retrieved from the ChunkSnapshot, which can be retrieved from a Chunk.

Which means, when a chunk is loaded (the same as we was doing before), we could count how many times a block id occurs, without actually iterating over each block, we could just retrieve each stored block id and count the occurances, making the counting very cheap. This requires no NMS whatsoever, no tinkering involved. We could use asynchronous counting to make the performance even better, since we are dealing with just a container of data.

## Conclusion

The performance overhead of reflection and the time it would potentially take to upkeep the custom Spigot meant my first solution just wasn't my best choice.

I went with the final solution to save resources and precious time.
