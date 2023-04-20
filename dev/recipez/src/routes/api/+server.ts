import type { RequestHandler } from './$types';
import axios from 'axios';
import cheerio from 'cheerio';


interface RecipeLinks {
    recipeLinks: string[];
}

export const POST: RequestHandler = async ({request}) => {
    const { recipeLinks } = await request.json();

    if (!recipeLinks || recipeLinks.length === 0) {
        return new Response('No recipe links provided', {
            status: 400
        });
    }

    try {
        const groceryList = await generateGroceryList(recipeLinks);
        return new Response(JSON.stringify(groceryList), {
            status: 200,
            headers: {
                'Content-Type': 'application/json'
            }
        });
    } catch (error) {
        console.error(error);
        return new Response('Failed to generate grocery list', {
            status: 500
        });
    }
};

async function generateGroceryList(recipeLinks : string[]) {
    const ingredientsList : string[] = [];
    for (const link of recipeLinks) {
        const ingredients = await fetchIngredients(link);
        ingredientsList.push(...ingredients);
    }

    const groceryList = consolidateGroceryList(ingredientsList);
    return groceryList;
}

async function fetchIngredients(url : string) {
    try {
        const response = await axios.get(url);
        const $ = cheerio.load(response.data);
        const ingredients : string[] = [];

        $('span[data-ingredient-name="true"]').each((_, element) => {
            ingredients.push($(element).text().trim());
        });

        return ingredients;
    } catch (error) {
        console.error(`Failed to fetch ingredients for ${url}: `, error);
        throw error;
    }
}

function consolidateGroceryList(ingredientsList : string[]) {
    // Here, you can implement a function to consolidate duplicate ingredients,
    // combine quantities, or process the ingredients list in any other way you'd like.
    // For now, we'll simply return the ingredients as-is.
    return ingredientsList;
}
