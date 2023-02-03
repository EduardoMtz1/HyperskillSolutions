package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.findRecipeById(id);
        if (recipe == null){
            return new ResponseEntity<>(Map.of("Error", "Recipe not found"), HttpStatus.valueOf(404));
        }
        return new ResponseEntity<>(recipe, HttpStatus.OK);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> postRecipe(@Valid @RequestBody Recipe body,
                                        @AuthenticationPrincipal UserDetails details) {
        User authUser = new User();
        authUser.setEmail(details.getUsername());
        authUser.setPassword(details.getPassword());
        body.setUser(authUser);
        body.setDate(LocalDateTime.now());
        Recipe recipe = recipeService.save(body);
        return new ResponseEntity<>(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@Valid @RequestBody Recipe body,
                                          @PathVariable long id,
                                          @AuthenticationPrincipal UserDetails details) {
        User authUser = new User();
        authUser.setEmail(details.getUsername());
        authUser.setPassword(details.getPassword());
        Recipe recipe = recipeService.findRecipeById(id);
        Recipe recipeWithUser = recipeService.findRecipeByIdAndUser(id, authUser);
        if (recipe == null){
            return new ResponseEntity<>(Map.of("Error", "Recipe not found"), HttpStatus.valueOf(404));
        }
        if (recipeWithUser == null) {
            return new ResponseEntity<>(Map.of("Error", "Access denied"), HttpStatus.FORBIDDEN);
        }
        recipeWithUser.setId(id);
        recipeWithUser.setName(body.getName());
        recipeWithUser.setDescription(body.getDescription());
        recipeWithUser.setDate(LocalDateTime.now());
        recipeWithUser.setCategory(body.getCategory());
        recipeWithUser.setIngredients(body.getIngredients());
        recipeWithUser.setDirections(body.getDirections());
        recipeService.save(recipeWithUser);
        return new ResponseEntity<>(Map.of("Ok", "Recipe updated"), HttpStatus.valueOf(204));
    }
    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable long id,
                                          @AuthenticationPrincipal UserDetails details) {
        User authUser = new User();
        authUser.setEmail(details.getUsername());
        authUser.setPassword(details.getPassword());
        Recipe recipe = recipeService.findRecipeById(id);
        Recipe recipeWithUser = recipeService.findRecipeByIdAndUser(id, authUser);
        if (recipe == null){
            return new ResponseEntity<>(Map.of("Error", "Recipe not found"), HttpStatus.valueOf(404));
        }
        if (recipeWithUser == null) {
            return new ResponseEntity<>(Map.of("Error", "Access denied"), HttpStatus.FORBIDDEN);
        }
        recipeService.deleteRecipeById(id);
        return new ResponseEntity<>(Map.of("Ok", "Recipe deleted"), HttpStatus.valueOf(204));

    }

    @GetMapping("/api/recipe/search")
    public ResponseEntity<?> searchRecipe(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        if(!(name == null ^ category == null)){
            return new ResponseEntity<>(Map.of("Error", "Bad request"), HttpStatus.valueOf(400));
        }
        List<Recipe> result;
        if(name != null) {
            result =  recipeService.findByName(name);
        } else {
            result =  recipeService.findByCategory(category);
        }
        return  new ResponseEntity<>( result, HttpStatus.OK);

    }

}

