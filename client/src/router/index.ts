/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router/auto';

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
});

function hasQueryParameters(route) {
  return !!Object.keys(route.query).length;
}

router.beforeEach((to, from, next) => {
  if (!hasQueryParameters(to) && hasQueryParameters(from)) {
    const toWithQuery = Object.assign({}, to, { query: from.query });
    next(toWithQuery);
  } else {
    next();
  }
})

export default router
